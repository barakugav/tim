package com.barakugav.tim;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Function;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

class TIMXML extends TIMInMem {

    private final String path;

    private static final String ROOT_TAG = "CalendarXRepository";
    private static final String TEMPLATES_FOLDER_TAG = "TemplatesFolder";
    private static final String INSTANCES_FOLDER_TAG = "InstancesFolder";

    private static final String TABLE_TAG = "Table";
    private static final String TEMPLATE_TAG = "Template";
    private static final String INSTANCE_TAG = "Instance";

    private static final String ID_REF_TAG = "IDRef";
    private static final String TEMPLATE_REF_TAG = "TemplateRef";
    private static final String INSTANCE_REFS_TAG = "InstanceRefs";
    private static final String INSTANCE_REF_TAG = "InstanceRef";

    private static final String ID_TAG = "ID";
    private static final String NAME_TAG = "Name";
    private static final String PROPERTIES_TAG = "Properties";
    private static final String PROPERTY_TAG = "Propery";
    private static final String KEY_TAG = "Key";
    private static final String VALUE_TAG = "Value";
    private static final String DATA_TAG = "Data";
    private static final String DATA_TYPE_TAG = "DataType";

    private static final String NO_VALUE = "__NO_VALUE__";

    TIMXML() {
	this("repository/test.xml");// TODO
    }

    TIMXML(String path) {
	this(path, "TIMXML");
    }

    TIMXML(String path, String name) {
	super(name);
	// CXUtils.requireValidPath(path);
	this.path = path;
    }

    @Override
    public void open() {
	if (isOpen())
	    return;
	super.open();
	try {
	    read(path);
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
    }

    @Override
    public void close() {
	if (!isOpen())
	    return;
	List<Exception> ex = new ArrayList<>(0);
	try {
	    write(path);
	} catch (ParserConfigurationException | TransformerException e) {
	    ex.add(e);
	}
	try {
	    super.close();
	} catch (Exception e) {
	    ex.add(e);
	}
	if (!ex.isEmpty()) {
	    for (Exception e : ex)
		e.printStackTrace();
	    throw new RuntimeException(ex.get(0));
	}
    }

    private void read(String filePath) throws SAXException, IOException, ParserConfigurationException, ParseException {
	File file = new File(filePath);
	if (!file.exists())
	    return;
	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	Document doc = dBuilder.parse(file);

	Element root = getChildByTag(doc, ROOT_TAG);
	for (Element tableElm : getChildrenByTag(root, TABLE_TAG))
	    readTable(tableElm);
    }

    private void write(String filePath) throws ParserConfigurationException, TransformerException {
	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	DocumentBuilder builder = factory.newDocumentBuilder();
	Document doc = builder.newDocument();

	Element rootElm = doc.createElement(ROOT_TAG);

	for (String tableName : tableNames())
	    writeTable(rootElm, tableName);

	doc.appendChild(rootElm);

	TransformerFactory transformerFactory = TransformerFactory.newInstance();
	Transformer transformer = transformerFactory.newTransformer();
	transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
	DOMSource domSource = new DOMSource(doc);
	StreamResult streamResult = new StreamResult(new File(filePath));
	transformer.transform(domSource, streamResult);

	// TODO - compact xml tags
    }

    private void writeTable(Element parent, String tableName) {
	Element tableElm = parent.getOwnerDocument().createElement(TABLE_TAG);
	tableElm.setAttribute(NAME_TAG, writeValue(tableName));
	writeTemplatesFolder(tableElm, getTemplates0(tableName));
	writeInstancesFolder(tableElm, getInstances0(tableName));
	parent.appendChild(tableElm);
    }

    private void readTable(Element tableElm) throws ParseException {
	Element templatesFolderElm = getChildByTag(tableElm, TEMPLATES_FOLDER_TAG);
	Element instancesFolderElm = getChildByTag(tableElm, INSTANCES_FOLDER_TAG);
	readTemplatesFolder(templatesFolderElm);
	readInstancesFolder(instancesFolderElm);
    }

    private static void writeTemplatesFolder(Element parent, Collection<Template0> templates) {
	Element templatesFolderElm = parent.getOwnerDocument().createElement(TEMPLATES_FOLDER_TAG);

	for (Template0 template : templates)
	    writeTemplate(templatesFolderElm, template);

	parent.appendChild(templatesFolderElm);
    }

    private static void writeInstancesFolder(Element parent, Collection<Instance0> instances) {
	Element instancesFolder = parent.getOwnerDocument().createElement(INSTANCES_FOLDER_TAG);

	for (Instance0 instance : instances)
	    writeInstance(instancesFolder, instance);

	parent.appendChild(instancesFolder);
    }

    private void readTemplatesFolder(Element templatesFolderElm) throws ParseException {
	for (Element templateElm : getChildrenByTag(templatesFolderElm, TEMPLATE_TAG))
	    readTemplate(templateElm);
    }

    private void readInstancesFolder(Element instancesFolderElm) throws ParseException {
	for (Element instanceElm : getChildrenByTag(instancesFolderElm, INSTANCE_TAG))
	    readInstance(instanceElm);
    }

    private static void writeTemplate(Element parent, Template0 template) {
	Element templateElm = parent.getOwnerDocument().createElement(TEMPLATE_TAG);
	writeID(template, templateElm);
	writeProperties(template, templateElm);
	writeTemplateInstances(template, templateElm);
	parent.appendChild(templateElm);
    }

    private static void writeInstance(Element parent, Instance instance) {
	Element instanceElm = parent.getOwnerDocument().createElement(INSTANCE_TAG);
	writeID(instance, instanceElm);
	writeProperties(instance, instanceElm);
	writeInstanceTemplate(instance, instanceElm);
	parent.appendChild(instanceElm);
    }

    private void readTemplate(Element templateElm) throws ParseException {
	ID id = readID(templateElm);
	Map<String, Object> properties = readProperties(templateElm);
	Template0 template = getOrCreateEmptyTemplate(id);
	template.setProperties(properties);
	template.setInstances(readTemplateInstances(templateElm));
    }

    private void readInstance(Element instanceElm) throws ParseException {
	ID id = readID(instanceElm);
	Map<String, Object> properties = readProperties(instanceElm);
	Instance0 instance = getOrCreateEmptyInstance(id);
	ID template = readInstanceTemplate(instanceElm);
	instance.setTemplate(template);
	instance.setProperties(properties);
    }

    private static void writeID(Atom atom, Element atomElm) {
	atomElm.setAttribute(ID_TAG, atom.getID().toString());
    }

    private static ID readID(Element atomElm) throws ParseException {
	return ID.valueOf(atomElm.getAttribute(ID_TAG));
    }

    private static void writeProperties(Atom atom, Element atomElm) {
	Element propertiesElm = atomElm.getOwnerDocument().createElement(PROPERTIES_TAG);
	for (Map.Entry<String, Object> property : atom.getProperties().entrySet())
	    writeProperty(propertiesElm, property.getKey(), property.getValue());
	atomElm.appendChild(propertiesElm);
    }

    private static void writeProperty(Element propertiesElm, String key, Object value) {
	if (key == null || key.isBlank())
	    return;
	Element propertyElm = propertiesElm.getOwnerDocument().createElement(PROPERTY_TAG);
	propertyElm.setAttribute(KEY_TAG, writeValue(key));
	writeData(propertyElm, value);
	propertiesElm.appendChild(propertyElm);
    }

    private static void writeInstanceTemplate(Instance instance, Element instanceElm) {
	instanceElm.setAttribute(TEMPLATE_REF_TAG, instance.getTemplate().getID().toString());
    }

    private static ID readInstanceTemplate(Element instanceElm) throws ParseException {
	return ID.valueOf(instanceElm.getAttribute(TEMPLATE_REF_TAG));
    }

    private static void writeTemplateInstances(Template0 template, Element templateElm) {
	Element instanceRefsElm = templateElm.getOwnerDocument().createElement(INSTANCE_REFS_TAG);
	for (ID instance : template.getInstances0()) {
	    Element instanceRefElm = instanceRefsElm.getOwnerDocument().createElement(INSTANCE_REF_TAG);
	    instanceRefElm.setAttribute(ID_REF_TAG, instance.toString());
	    instanceRefsElm.appendChild(instanceRefElm);
	}
	templateElm.appendChild(instanceRefsElm);
    }

    private static Collection<ID> readTemplateInstances(Element templateElm) throws ParseException {
	Collection<ID> instances = new ArrayList<>();
	Element instanceRefsElm = getChildByTag(templateElm, INSTANCE_REFS_TAG);
	for (Element instanceRefElm : getChildrenByTag(instanceRefsElm, INSTANCE_REF_TAG))
	    instances.add(ID.valueOf(instanceRefElm.getAttribute(ID_REF_TAG)));
	return instances;
    }

    private static enum DataType {

	NullDT {
	    @Override
	    void writeData0(Element dataElm, Object data) {
	    }

	    @Override
	    Void readData(Element dataElm, TIMInMem model) {
		return null;
	    }
	},
	BooleanDT {
	    @Override
	    void writeData0(Element dataElm, Object data) {
		dataElm.setAttribute(DATA_TAG, ((Boolean) data).toString());
	    }

	    @Override
	    Boolean readData(Element dataElm, TIMInMem model) {
		return Boolean.valueOf(dataElm.getAttribute(DATA_TAG));
	    }
	},
	ByteDT {
	    @Override
	    void writeData0(Element dataElm, Object data) {
		dataElm.setAttribute(DATA_TAG, Integer.toString(((Byte) data).intValue(), RADIX));
	    }

	    @Override
	    Byte readData(Element dataElm, TIMInMem model) {
		return Byte.valueOf(dataElm.getAttribute(DATA_TAG), RADIX);
	    }
	},
	ShortDT {
	    @Override
	    void writeData0(Element dataElm, Object data) {
		dataElm.setAttribute(DATA_TAG, Integer.toString(((Short) data).intValue(), RADIX));
	    }

	    @Override
	    Short readData(Element dataElm, TIMInMem model) {
		return Short.valueOf(dataElm.getAttribute(DATA_TAG), RADIX);
	    }
	},
	IntegerDT {
	    @Override
	    void writeData0(Element dataElm, Object data) {
		dataElm.setAttribute(DATA_TAG, Integer.toString(((Integer) data).intValue(), RADIX));
	    }

	    @Override
	    Integer readData(Element dataElm, TIMInMem model) {
		return Integer.valueOf(dataElm.getAttribute(DATA_TAG), RADIX);
	    }
	},
	LongDT {
	    @Override
	    void writeData0(Element dataElm, Object data) {
		dataElm.setAttribute(DATA_TAG, Long.toString(((Long) data).longValue(), RADIX));
	    }

	    @Override
	    Long readData(Element dataElm, TIMInMem model) {
		return Long.valueOf(dataElm.getAttribute(DATA_TAG), RADIX);
	    }
	},
	FloatDT {
	    @Override
	    void writeData0(Element dataElm, Object data) {
		dataElm.setAttribute(DATA_TAG, ((Float) data).toString());
	    }

	    @Override
	    Float readData(Element dataElm, TIMInMem model) {
		return Float.valueOf(dataElm.getAttribute(DATA_TAG));
	    }
	},
	DoubleDT {
	    @Override
	    void writeData0(Element dataElm, Object data) {
		dataElm.setAttribute(DATA_TAG, ((Double) data).toString());
	    }

	    @Override
	    Double readData(Element dataElm, TIMInMem model) {
		return Double.valueOf(dataElm.getAttribute(DATA_TAG));
	    }
	},
	CharacterDT {
	    @Override
	    void writeData0(Element dataElm, Object data) {
		dataElm.setAttribute(DATA_TAG, ((Character) data).toString());
	    }

	    @Override
	    Character readData(Element dataElm, TIMInMem model) {
		String charStr = dataElm.getAttribute(DATA_TAG);
		if (charStr.length() != 1)
		    throw new IllegalArgumentException();
		return Character.valueOf(charStr.charAt(0));
	    }
	},
	BigIntegerDT {
	    @Override
	    void writeData0(Element dataElm, Object data) {
		dataElm.setAttribute(DATA_TAG, ((BigInteger) data).toString(RADIX));
	    }

	    @Override
	    BigInteger readData(Element dataElm, TIMInMem model) {
		return new BigInteger(dataElm.getAttribute(DATA_TAG), RADIX);
	    }
	},
	BigDecimalDT {
	    @Override
	    void writeData0(Element dataElm, Object data) {
		dataElm.setAttribute(DATA_TAG, ((BigDecimal) data).toString());
	    }

	    @Override
	    BigDecimal readData(Element dataElm, TIMInMem model) {
		return new BigDecimal(dataElm.getAttribute(DATA_TAG));
	    }
	},
	BooleanArrayDT {
	    @Override
	    void writeData0(Element dataElm, Object data) {
		boolean[] d = (boolean[]) data;
		char[] c = new char[d.length];
		for (int i = 0; i < d.length; i++)
		    c[i] = d[i] ? 't' : 'f';
		dataElm.setAttribute(DATA_TAG, new String(c));
	    }

	    @Override
	    boolean[] readData(Element dataElm, TIMInMem model) {
		char[] c = dataElm.getAttribute(DATA_TAG).toCharArray();
		boolean[] d = new boolean[c.length];
		for (int i = 0; i < d.length; i++)
		    d[i] = c[i] == 't';
		return d;
	    }
	},
	ByteArrayDT {
	    @Override
	    void writeData0(Element dataElm, Object data) {
		byte[] d = (byte[]) data;
		String st;
		if (d.length == 0)
		    st = "";
		else {
		    StringBuilder s = new StringBuilder(d.length * 3);
		    for (int i = 0; i < d.length - 1; i++)
			s.append(Integer.toString(d[i], RADIX)).append(' ');
		    s.append(Integer.toString(d[d.length - 1], RADIX));
		    st = s.toString();
		}
		dataElm.setAttribute(DATA_TAG, st);
	    }

	    @Override
	    byte[] readData(Element dataElm, TIMInMem model) {
		String[] s = dataElm.getAttribute(DATA_TAG).split(" ");
		byte[] d = new byte[s.length];
		for (int i = 0; i < s.length; i++)
		    d[i] = Byte.parseByte(s[i], RADIX);
		return d;
	    }
	},
	ShortArrayDT {
	    @Override
	    void writeData0(Element dataElm, Object data) {
		short[] d = (short[]) data;
		String st;
		if (d.length == 0)
		    st = "";
		else {
		    StringBuilder s = new StringBuilder(d.length * 5);
		    for (int i = 0; i < d.length - 1; i++)
			s.append(Integer.toString(d[i], RADIX)).append(' ');
		    s.append(Integer.toString(d[d.length - 1], RADIX));
		    st = s.toString();
		}
		dataElm.setAttribute(DATA_TAG, st);
	    }

	    @Override
	    short[] readData(Element dataElm, TIMInMem model) {
		String[] s = dataElm.getAttribute(DATA_TAG).split(" ");
		short[] d = new short[s.length];
		for (int i = 0; i < s.length; i++)
		    d[i] = Short.parseShort(s[i], RADIX);
		return d;
	    }
	},
	IntegerArrayDT {
	    @Override
	    void writeData0(Element dataElm, Object data) {
		int[] d = (int[]) data;
		String st;
		if (d.length == 0)
		    st = "";
		else {
		    StringBuilder s = new StringBuilder(d.length * 9);
		    for (int i = 0; i < d.length - 1; i++)
			s.append(Integer.toString(d[i], RADIX)).append(' ');
		    s.append(Integer.toString(d[d.length - 1], RADIX));
		    st = s.toString();
		}
		dataElm.setAttribute(DATA_TAG, st);
	    }

	    @Override
	    int[] readData(Element dataElm, TIMInMem model) {
		String[] s = dataElm.getAttribute(DATA_TAG).split(" ");
		int[] d = new int[s.length];
		for (int i = 0; i < s.length; i++)
		    d[i] = Integer.parseInt(s[i], RADIX);
		return d;
	    }
	},
	LongArrayDT {
	    @Override
	    void writeData0(Element dataElm, Object data) {
		long[] d = (long[]) data;
		String st;
		if (d.length == 0)
		    st = "";
		else {
		    StringBuilder s = new StringBuilder(d.length * 17);
		    for (int i = 0; i < d.length - 1; i++)
			s.append(Long.toString(d[i], RADIX)).append(' ');
		    s.append(Long.toString(d[d.length - 1], RADIX));
		    st = s.toString();
		}
		dataElm.setAttribute(DATA_TAG, st);
	    }

	    @Override
	    long[] readData(Element dataElm, TIMInMem model) {
		String[] s = dataElm.getAttribute(DATA_TAG).split(" ");
		long[] d = new long[s.length];
		for (int i = 0; i < s.length; i++)
		    d[i] = Long.parseLong(s[i], RADIX);
		return d;
	    }
	},
	FloatArrayDT {
	    @Override
	    void writeData0(Element dataElm, Object data) {
		float[] d = (float[]) data;
		String st;
		if (d.length == 0)
		    st = "";
		else {
		    StringBuilder s = new StringBuilder(d.length * 9);
		    for (int i = 0; i < d.length - 1; i++)
			s.append(Float.toString(d[i])).append(' ');
		    s.append(Float.toString(d[d.length - 1]));
		    st = s.toString();
		}
		dataElm.setAttribute(DATA_TAG, st);
	    }

	    @Override
	    float[] readData(Element dataElm, TIMInMem model) {
		String[] s = dataElm.getAttribute(DATA_TAG).split(" ");
		float[] d = new float[s.length];
		for (int i = 0; i < s.length; i++)
		    d[i] = Float.parseFloat(s[i]);
		return d;
	    }
	},
	DoubleArrayDT {
	    @Override
	    void writeData0(Element dataElm, Object data) {
		double[] d = (double[]) data;
		String st;
		if (d.length == 0)
		    st = "";
		else {
		    StringBuilder s = new StringBuilder(d.length * 17);
		    for (int i = 0; i < d.length - 1; i++)
			s.append(Double.toString(d[i])).append(' ');
		    s.append(Double.toString(d[d.length - 1]));
		    st = s.toString();
		}
		dataElm.setAttribute(DATA_TAG, st);
	    }

	    @Override
	    double[] readData(Element dataElm, TIMInMem model) {
		String[] s = dataElm.getAttribute(DATA_TAG).split(" ");
		double[] d = new double[s.length];
		for (int i = 0; i < s.length; i++)
		    d[i] = Double.parseDouble(s[i]);
		return d;
	    }
	},
	CharacterArrayDT {
	    @Override
	    void writeData0(Element dataElm, Object data) {
		char[] d = (char[]) data;
		String st;
		if (d.length == 0)
		    st = "";
		else {
		    StringBuilder s = new StringBuilder(d.length * 2);
		    for (int i = 0; i < d.length - 1; i++)
			s.append(String.valueOf(d[i])).append(' ');
		    s.append(String.valueOf(d[d.length - 1]));
		    st = s.toString();
		}
		dataElm.setAttribute(DATA_TAG, st);
	    }

	    @Override
	    char[] readData(Element dataElm, TIMInMem model) {
		String[] s = dataElm.getAttribute(DATA_TAG).split(" ");
		char[] d = new char[s.length];
		for (int i = 0; i < s.length; i++) {
		    if (s[i].length() != 1)
			throw new IllegalArgumentException();
		    d[i] = s[i].charAt(0);
		}
		return d;
	    }
	},
	ObjectArrayDT {
	    @Override
	    void writeData0(Element dataElm, Object data) {
		Object[] d = (Object[]) data;
		writeCollection(dataElm, Arrays.asList(d));
	    }

	    @Override
	    Object[] readData(Element dataElm, TIMInMem model) throws ParseException {
		List<Object> result = new ArrayList<>();
		readCollection(dataElm, result, model);
		return result.toArray();
	    }
	},
	StringDT {
	    @Override
	    void writeData0(Element dataElm, Object data) {
		dataElm.setAttribute(DATA_TAG, (String) data);
	    }

	    @Override
	    String readData(Element dataElm, TIMInMem model) {
		return dataElm.getAttribute(DATA_TAG);
	    }
	},
	EnumDT {
	    @Override
	    void writeData0(Element dataElm, Object data) {
		Class<?> c = null;
		String name = null;
		if (data instanceof Enum) {
		    Enum<?> d = (Enum<?>) data;
		    c = d.getDeclaringClass();
		    name = d.name();
		} else if (data.getClass().isEnum()) {
		    c = data.getClass();
		    for (Object eObj : c.getEnumConstants())
			if (data.equals(eObj))
			    name = ((Enum<?>) eObj).name();
		}
		if (c == null || name == null)
		    throw new IllegalArgumentException();
		String s = c.getName() + ";" + name;
		dataElm.setAttribute(DATA_TAG, s);
	    }

	    @Override
	    Object readData(Element dataElm, TIMInMem model) {
		String[] s = dataElm.getAttribute(DATA_TAG).split(";");
		if (s.length == 2) {
		    try {
			String className = s[0];
			String name = s[1];
			for (Object eObj : Class.forName(className).getEnumConstants())
			    if (((Enum<?>) eObj).name().equals(name))
				return eObj;
		    } catch (ClassNotFoundException e) {
			throw new IllegalArgumentException(e);
		    }
		}
		throw new IllegalArgumentException();
	    }
	},
	IDDT {

	    @Override
	    void writeData0(Element dataElm, Object data) {
		dataElm.setAttribute(ID_REF_TAG, ((ID) data).toString());
	    }

	    @Override
	    ID readData(Element dataElm, TIMInMem model) throws ParseException {
		return ID.valueOf(dataElm.getAttribute(ID_REF_TAG));
	    }
	},
	TemplateDT {

	    @Override
	    void writeData0(Element dataElm, Object data) {
		dataElm.setAttribute(ID_REF_TAG, ((Template) data).getID().toString());
	    }

	    @Override
	    Object readData(Element dataElm, TIMInMem model) throws ParseException {
		return model.getOrCreateEmptyTemplate(ID.valueOf(dataElm.getAttribute(ID_REF_TAG)));
	    }
	},
	InstanceDT {

	    @Override
	    void writeData0(Element dataElm, Object data) {
		dataElm.setAttribute(ID_REF_TAG, ((Instance) data).getID().toString());
	    }

	    @Override
	    Instance readData(Element dataElm, TIMInMem model) throws ParseException {
		return model.getOrCreateEmptyInstance(ID.valueOf(dataElm.getAttribute(ID_REF_TAG)));
	    }
	},
	ListDT {
	    @Override
	    void writeData0(Element dataElm, Object data) {
		List<?> d = (List<?>) data;
		writeCollection(dataElm, d);
	    }

	    @Override
	    List<Object> readData(Element dataElm, TIMInMem model) throws ParseException {
		List<Object> result = new ArrayList<>();
		readCollection(dataElm, result, model);
		return result;
	    }
	},
	SetDT {
	    @Override
	    void writeData0(Element dataElm, Object data) {
		Set<?> d = (Set<?>) data;
		writeCollection(dataElm, d);
	    }

	    @Override
	    Set<Object> readData(Element dataElm, TIMInMem model) throws ParseException {
		Set<Object> result = new HashSet<>();
		readCollection(dataElm, result, model);
		return result;
	    }
	},
	MapDT {
	    @Override
	    void writeData0(Element dataElm, Object data) {
		Map<?, ?> d = (Map<?, ?>) data;
		for (Map.Entry<?, ?> entry : d.entrySet()) {
		    Element entryElm = dataElm.getOwnerDocument().createElement(DATA_TAG);
		    Element keyElm = entryElm.getOwnerDocument().createElement(KEY_TAG);
		    Element valueElm = entryElm.getOwnerDocument().createElement(VALUE_TAG);
		    Object key = entry.getKey(), value = entry.getValue();
		    typeOf(key).writeData(keyElm, key);
		    typeOf(value).writeData(valueElm, value);
		    entryElm.appendChild(keyElm);
		    entryElm.appendChild(valueElm);
		    dataElm.appendChild(entryElm);
		}
	    }

	    @Override
	    Map<Object, Object> readData(Element dataElm, TIMInMem model) throws ParseException {
		Map<Object, Object> result = new HashMap<>();
		for (Element entryElm : getChildrenByTag(dataElm, DATA_TAG)) {
		    Element keyElm = getChildByTag(entryElm, KEY_TAG);
		    Element valueElm = getChildByTag(entryElm, VALUE_TAG);
		    Object key = readDataType(keyElm).readData(keyElm, model);
		    Object value = readDataType(valueElm).readData(valueElm, model);
		    result.put(key, value);
		}
		return result;
	    }
	},
	UnknownDT {

	    @Override
	    void writeData0(Element dataElm, Object data) {
		throw new UnsupportedOperationException("Unsupported type: " + data.getClass());
	    }

	    @Override
	    Object readData(Element dataElm, TIMInMem model) {
		throw new UnsupportedOperationException("Unsupported type: " + readDataType(dataElm));
	    }

	};

	private static int RADIX = 16;

	void writeData(Element dataElm, Object data) {
	    writeDataType(dataElm, this);
	    writeData0(dataElm, data);
	}

	abstract void writeData0(Element dataElm, Object data);

	private static void writeCollection(Element folderElm, Collection<?> data) {
	    for (Iterator<?> it = data.iterator(); it.hasNext();) {
		Object obj = it.next();
		Element objElm = folderElm.getOwnerDocument().createElement(DATA_TAG);
		typeOf(obj).writeData(objElm, obj);
		folderElm.appendChild(objElm);
	    }
	}

	private static void readCollection(Element folderElm, Collection<Object> out, TIMInMem model)
		throws ParseException {
	    for (Element objElm : getChildrenByTag(folderElm, DATA_TAG))
		out.add(readDataType(objElm).readData(objElm, model));
	}

	abstract Object readData(Element dataElm, TIMInMem model) throws ParseException;

	static DataType typeOf(Object data) {
	    if (data == null)
		return NullDT;
	    if (data instanceof Boolean)
		return BooleanDT;
	    if (data instanceof Number)
		return typeOfNumber((Number) data);
	    if (data instanceof Character)
		return CharacterDT;
	    if (data.getClass().isArray())
		return typeOfArray(data);
	    if (data instanceof ID)
		return IDDT;
	    if (data instanceof Template)
		return TemplateDT;
	    if (data instanceof Instance)
		return InstanceDT;
	    if (data instanceof String)
		return StringDT;
	    if (data.getClass().isEnum() || data instanceof Enum)
		return EnumDT;
	    if (data instanceof List)
		return ListDT;
	    if (data instanceof Set)
		return SetDT;
	    if (data instanceof Map)
		return MapDT;
	    return UnknownDT;
	}

	private static DataType typeOfNumber(Number data) {
	    if (data instanceof Byte)
		return ByteDT;
	    if (data instanceof Short)
		return ShortDT;
	    if (data instanceof Integer)
		return IntegerDT;
	    if (data instanceof Long)
		return LongDT;
	    if (data instanceof Float)
		return FloatDT;
	    if (data instanceof Double)
		return DoubleDT;
	    if (data instanceof BigInteger)
		return BigIntegerDT;
	    if (data instanceof BigDecimal)
		return BigDecimalDT;
	    return UnknownDT;
	}

	private static DataType typeOfArray(Object data) {
	    if (data instanceof boolean[])
		return BooleanArrayDT;
	    if (data instanceof byte[])
		return ByteArrayDT;
	    if (data instanceof short[])
		return ShortArrayDT;
	    if (data instanceof int[])
		return IntegerArrayDT;
	    if (data instanceof long[])
		return LongArrayDT;
	    if (data instanceof float[])
		return FloatArrayDT;
	    if (data instanceof double[])
		return DoubleArrayDT;
	    if (data instanceof char[])
		return CharacterArrayDT;
	    if (data instanceof Object[])
		return ObjectArrayDT;
	    return UnknownDT;
	}

	private static void writeDataType(Element dataElm, DataType dataType) {
	    dataElm.setAttribute(DATA_TYPE_TAG, dataType.name());
	}

	private static DataType readDataType(Element dataElm) {
	    return DataType.valueOf(dataElm.getAttribute(DATA_TYPE_TAG));
	}

    }

    private static void writeData(Element dataElm, Object value) {
	DataType.typeOf(value).writeData(dataElm, value);
    }

    private Map<String, Object> readProperties(Element atomElm) throws ParseException {
	Element propertiesElm = getChildByTag(atomElm, PROPERTIES_TAG);
	Map<String, Object> properties = new HashMap<>();
	for (Element propertyElm : getChildrenByTag(propertiesElm, PROPERTY_TAG)) {
	    String key = readValue(propertyElm.getAttribute(KEY_TAG));
	    Object value = readData(propertyElm);
	    if (key != null && !key.isBlank())
		properties.put(key, value);
	}
	return properties;
    }

    private static String readValue(String value) {
	return NO_VALUE.equals(value) ? null : value;
    }

    private Object readData(Element dataElm) throws ParseException {
	return DataType.readDataType(dataElm).readData(dataElm, this);
    }

    private static String writeValue(Object value) {
	return writeValue(value, Object::toString);
    }

    private static Element getChildByTag(Node node, String tag) {
	for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling())
	    if (child instanceof Element && tag.equals(((Element) child).getTagName()))
		return (Element) child;
	return null;
    }

    private static Iterable<Element> getChildrenByTag(Node node, String tag) {
	return new Iterable<Element>() {

	    @Override
	    public Iterator<Element> iterator() {
		return new Iterator<Element>() {

		    Node curser;
		    boolean curserValid;

		    {
			curser = node.getFirstChild();
			curserValid = false;
			hasNext();
		    }

		    @Override
		    public boolean hasNext() {
			if (curserValid)
			    return true;
			for (; curser != null; advance())
			    if (curser instanceof Element && tag.equals(((Element) curser).getTagName()))
				return curserValid = true;
			return false;
		    }

		    @Override
		    public Element next() {
			if (!hasNext())
			    throw new NoSuchElementException();
			Element result = (Element) curser;
			advance();
			curserValid = false;
			return result;
		    }

		    private void advance() {
			curser = curser.getNextSibling();
		    }

		};
	    }
	};
    }

    private static <T> String writeValue(T value, Function<T, String> toString) {
	return value == null ? NO_VALUE : toString.apply(value);
    }

}
