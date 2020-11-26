package com.ishikawa.bigfilesreader;

import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class BigFilesReaderApplication implements CommandLineRunner {

	@Override
	public void run(String... args) throws Exception {
		final File file = new File("ip_addresses1.txt");
		createFileWithIPv4Addresses(file);

		readFileWithIPv4Addresses(file);
	}

	public static void main(String[] args) {
		SpringApplication.run(BigFilesReaderApplication.class, args);
	}

	private static void readFileWithIPv4Addresses(File file) {
		System.out.println("Start read file " + file.getName());
		final long start = new Date().getTime();
		Set<Long> ips = new HashSet<>();
		try (BufferedReader fileInputStream = new BufferedReader(new FileReader(file))) {

			String s = null;
			while ((s = fileInputStream.readLine()) != null){
				long ipNumbers = convertIPv4ToLong(s);
				ips.add(ipNumbers);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("End read file " + file.getName());
		final long end = new Date().getTime();


		System.out.println(ips.size() + " count");
		System.out.println(end - start + " ms");
	}

	private static long convertIPv4ToLong(String s) {
		int[] ip = new int[4];
		String[] parts = s.split("\\.");

		long ipNumbers = 0;
		for (int i = 0; i < 4; i++) {
			ip[i] = Integer.parseInt(parts[i]);
			ipNumbers += ip[i] << (24 - (8 * i));
		}
		return ipNumbers;
	}

	private static void createFileWithIPv4Addresses(File file) {
		final Faker faker = new Faker();
		System.out.println("Start process file " + file.getName());
		final long start = new Date().getTime();
		try(BufferedWriter fileOutputStream = new BufferedWriter(new FileWriter(file))) {
			for (int i = 0; i < 100_000_000; i++) {
				final String ipV4Address = faker.internet().ipV4Address();
				fileOutputStream.write((ipV4Address+"\n"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("End process file " + file.getName());

		final long end = new Date().getTime();

		final String fileSize = String.format("%.4f mb", (double) file.length() / (1024 * 1024));
		System.out.println(fileSize);
		System.out.println(end - start + " ms");
	}


}
