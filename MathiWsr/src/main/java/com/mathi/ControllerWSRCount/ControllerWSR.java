package com.mathi.ControllerWSRCount;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class ControllerWSR {
	private static final DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("yyyyMMdd'T'HHmmss'Z'")
			.withZoneUTC();

	@Autowired
	private RestTemplateBuilder restTemplate;

	@GetMapping("/hie")
	public void a1() {
		downloadFile();
	}

	String secretKey = "om/4MpLoXzUIhn8Zd6KB9Ozkk4kghZJewYrJ92Cs";
	String dateStamp = "20200420";
	String regionName = "ap-south-1";
	String serviceName = "execute-api";

	/*
	 * @RequestMapping(path = "/download", method = RequestMethod.GET) public
	 * ResponseEntity<Resource> download(@RequestParam("image") String image) throws
	 * IOException { File file = new File(SERVER_LOCATION + File.separator + image +
	 * EXTENSION);
	 * 
	 * HttpHeaders header = new HttpHeaders();
	 * header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=img.jpg");
	 * header.add("Cache-Control", "no-cache, no-store, must-revalidate");
	 * header.add("Pragma", "no-cache"); header.add("Expires", "0");
	 * 
	 * Path path = Paths.get(file.getAbsolutePath()); ByteArrayResource resource =
	 * new ByteArrayResource(Files.readAllBytes(path));
	 * 
	 * return ResponseEntity.ok() .headers(header) .contentLength(file.length())
	 * .contentType(MediaType.parseMediaType("application/octet-stream"))
	 * .body(resource); }
	 */

	static byte[] HmacSHA256(String data, byte[] key) throws Exception {
		String algorithm = "HmacSHA256";
		Mac mac = Mac.getInstance(algorithm);
		mac.init(new SecretKeySpec(key, algorithm));
		return mac.doFinal(data.getBytes("UTF-8"));
	}

	public static byte[] getSha256(String value) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			return md.digest(value.getBytes("UTF-8"));
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	static byte[] getSignatureKey(String dateStamp, String regionName, String serviceName) throws Exception {
		byte[] kSecret = ("AWS4" + "om/4MpLoXzUIhn8Zd6KB9Ozkk4kghZJewYrJ92Cs").getBytes("UTF-8");
		byte[] kDate = HmacSHA256(dateStamp, kSecret);
		byte[] kRegion = HmacSHA256(regionName, kDate);
		byte[] kService = HmacSHA256(serviceName, kRegion);
		byte[] kSigning = HmacSHA256("aws4_request", kService);
		String canonicalRequest = "GET" + "\n" + "/" + "\n" + "" + "\n" + "host:test-mathi.s3.ap-south-1.amazonaws.com"
				+ "\n" + "x-amz-content-sha256:e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855" + "\n"
				+ "x-amz-date:" + timeFormatter.print(new Date().getTime()) + "\n"
				+ "host;x-amz-content-sha256;x-amz-date" + "\n"
				+ "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";
		String StrigntoSign = "AWS4-HMAC-SHA256" + "\n" + timeFormatter.print(new Date().getTime()) + "\n"
				+ "20200420/ap-south-1/s3/aws4_request" + "\n" + Hex.encodeHexString(getSha256(canonicalRequest));
		return HmacSHA256(StrigntoSign, kSigning);
	}

	public void downloadFile() { // This method will download file using RestTemplate
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
			// getSha256("");
			// System.out.println(bytesToHex(getSha256("")));
			// System.out.println(getSignatureKey( dateStamp, regionName, serviceName));
			// System.out.println(getSignatureKey( dateStamp, regionName, serviceName));
			System.out.println(Hex.encodeHexString(getSignatureKey(dateStamp, regionName, serviceName)));
			headers.add("Authorization",
					"AWS4-HMAC-SHA256 Credential=<AccessKey>/20200420/ap-south-1/s3/aws4_request,SignedHeaders=host;x-amz-content-sha256;x-amz-date,Signature="
							+ Hex.encodeHexString(getSignatureKey(dateStamp, regionName, serviceName)));
			headers.add("X-Amz-Content-Sha256", "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855");
			headers.add("X-Amz-Date", timeFormatter.print(new Date().getTime()));
			HttpEntity<String> entity = new HttpEntity<>(headers);
			ResponseEntity<byte[]> response = restTemplate.build().exchange(
					"https://test-mathi.s3.ap-south-1.amazonaws.com/DSC_0115.JPG", HttpMethod.GET, entity,
					byte[].class);
			Files.write(Paths.get("C:\\Users\\Mathi\\Downloads"), response.getBody());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
