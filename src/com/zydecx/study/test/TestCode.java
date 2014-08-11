package com.zydecx.study.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.util.Scanner;
import java.util.SortedMap;

public class TestCode {
	private static int aa = 1;
	public static String bb = "22a";
	
	
	public static void main(String[] args) {
		//fileClassTest();
		//readFromConsole(2);
		listCharset();
		try {
			WriteByChannel();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void readFromConsole(int type) {
		if (type == 1) {
			readFromConsoleByScanner();
		} else if (type == 2) {
			try {
				readFromConsoleByInputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private static void readFromConsoleByScanner() {
		Scanner sc = new Scanner(System.in);
		String str;
		System.out.print("Input some string: ");
		while (sc.hasNext()) {
			str = sc.nextLine();
			System.out.println("Your input: " + str);
			System.out.print("\nInput some string: ");
		}
	}
	
	private static void readFromConsoleByInputStream() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String str;
		System.out.println("Input some string: ");
		while ((str = br.readLine()) != null) {
			System.out.println("Your input: " + str);
			System.out.print("\nInput some string: ");
		}
	}
	
	public static void fileClassTest() {
		File file = new File(".");
		println(file.getName());
		println(file.getAbsolutePath());
		println(file.getParent());
		println(file.getAbsoluteFile().getParent());
		
		try {
			File tmpFile = File.createTempFile("test", null, file);
			tmpFile.deleteOnExit();
			println(tmpFile.getAbsolutePath());
			for (String s : tmpFile.getParentFile().list()) {
				println(s);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (File f : File.listRoots()) {
			println(f.getAbsolutePath());
		}
		
	}
	
	public static void writeToFile() throws IOException {
		File f = new File("test.txt");
		if (!f.exists()) f.createNewFile();
		try (
			FileOutputStream fos = new FileOutputStream(f);
			PrintStream ps = new PrintStream(fos)) {
				ps.println("hahha");
				ps.println(new TestCode());
			}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void writeToApplication() throws IOException {
		Process p = Runtime.getRuntime().exec("java TestCodeInner");
		try (
			PrintStream br = new PrintStream(p.getOutputStream())) {
			br.println("hello");
			br.println("¹þHHH");
		}
	}
	
	public static void readAndWriteObjectToFile() throws IOException {
		Person p = new Person("jer", 28);
		File f = new File("person.obj");
		if (!f.exists()) f.createNewFile();
		
		try (
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f))
				) {
			oos.writeObject(p);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try (
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
				) {
			Person p2 = (Person) ois.readObject();
			System.out.println("New Person Object: name-" + p2.getName() + ", age-" + p2.getAge());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void readAndWriteByNIO() {
		CharBuffer cb = CharBuffer.allocate(10);
		println(cb.capacity());
		println(cb.position());
		println(cb.limit());
		println(cb.remaining());
		
		cb.put("hello");
		println("After 'hello',");
		println(cb.capacity());
		println(cb.position());
		println(cb.limit());
		println(cb.remaining());
		
		cb.flip();
		println("After flip,");
		println(cb.capacity());
		println(cb.position());
		println(cb.limit());
		println(cb.remaining());
		
		cb.clear();
		
		cb.put("helloworld");
		println("After 'helloworld',");
		println(cb.capacity());
		println(cb.position());
		println(cb.limit());
		println(cb.remaining());
		
		
		cb.clear();
		
		
	}
	
	public static void WriteByChannel() throws IOException {
		File f = new File("tmp9080008671199742408.tmp");
		try (
				RandomAccessFile raf = new RandomAccessFile(f, "rw");
				FileChannel c = raf.getChannel()
				) {
			ByteBuffer bb2 = c.map(FileChannel.MapMode.READ_ONLY, 0, f.length());
			ByteBuffer bb = ByteBuffer.allocate(64);
			while (c.read(bb) != -1) {
				bb.flip();
				System.out.println(bb.limit());
				Charset cs = Charset.forName("utf-8");
				CharsetDecoder cd = cs.newDecoder();
				cd.onMalformedInput(CodingErrorAction.IGNORE);
				CharBuffer cb = cs.decode(bb);
				System.out.println(cb);
				
				bb.clear();
			}
			System.out.println(bb2);
		}
	}
	
	public static void listCharset() {
		System.out.println(System.getProperty("file.encoding"));
		SortedMap<String, Charset> sm = Charset.availableCharsets();
		for (String c : sm.keySet()) {
			System.out.println(c);
		}
	}
	
	private static void println(String s) {
		System.out.println(s);
	}
	
	private static void println(int s) {
		System.out.println(s);
	}
}

class Person implements java.io.Serializable {
	private String name;
	private int age;
	
	public Person(String name, int age) {
		System.out.println("New Person object: name-" + name + ", age-" + age + " has been created.");
		this.name = name;
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
}