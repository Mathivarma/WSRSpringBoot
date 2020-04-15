package com.mathi.ControllerWSRCount;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.codec.binary.Hex;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mathi.model.Counter;
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class ControllerWSR {
	@Autowired
	Counter counter;
	   @Autowired
	   private RestTemplateBuilder restTemplate;
	@PostMapping("/WsrCount")
	public Counter WRSCount(@RequestBody  String word) {
		

		final String regex = "(US+\\d+|DE+\\d+)";
		final Pattern pattern = Pattern.compile(regex);
		final Matcher matcher = pattern.matcher(word);
		List<String> AllUS = new LinkedList<>();
		while (matcher.find()) {
			AllUS.add(matcher.group(0));
		}
		AllUS=AllUS.stream().filter(i->i.length()>0).distinct().collect(Collectors.toList());
		AllUS.forEach(System.out::println);
		List<String> US1=AllUS.stream().filter(a->a.matches("^US.*")).collect(Collectors.toList());
		List<String> DE1=AllUS.stream().filter(a->a.matches("^DE.*")).collect(Collectors.toList());
		System.out.println(String.valueOf(AllUS.size())+String.valueOf(US1.size())+String.valueOf(DE1.size()));
		counter.setALLUS(AllUS.size());
		counter.setDefectCount(DE1.size());
		counter.setUScount(US1.size());
		return counter;
	}
	@GetMapping("/hi")
	public String a() {
		    byte[] signature;
			try {
				//signature = getSignatureKey(secretKey, dateStamp, regionName, serviceName);
			//	System.out.println("Signature : " + Hex.encodeHexString(signature));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    
		return "a";
	
	}
	@GetMapping("/hie")
	public void a1() {
		downloadFile();
	}
    public static String byteArrayToHexString(byte[] byteArray){
        String hexString = "";

        for(int i = 0; i < byteArray.length; i++){
            String thisByte = "".format("%x", byteArray[i]);
            hexString += thisByte;
        }

        return hexString;
    }
    
    String dateStamp = "20200404";
    String regionName = "ap-south-1";
    String serviceName = "execute-api";
    private static final String EXTENSION = ".jpg";
    private static final String SERVER_LOCATION = "/server/images";

    @RequestMapping(path = "/download", method = RequestMethod.GET)
    public ResponseEntity<Resource> download(@RequestParam("image") String image) throws IOException {
        File file = new File(SERVER_LOCATION + File.separator + image + EXTENSION);

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=img.jpg");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        return ResponseEntity.ok()
                .headers(header)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }

	static byte[] HmacSHA256(String data, byte[] key) throws Exception {
	    String algorithm="HmacSHA256";
	    Mac mac = Mac.getInstance(algorithm);
	    mac.init(new SecretKeySpec(key, algorithm));
	    return mac.doFinal(data.getBytes("UTF-8"));
	}

	static byte[] getSignatureKey(String key, String dateStamp, String regionName, String serviceName) throws Exception {
	   
	 
	    return null;
	}
	   public void downloadFile(){     // This method will download file using RestTemplate
	       try {
	           HttpHeaders headers = new HttpHeaders();
	           headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
	           
	           //headers.add("X-Amz-Date", "20200405T054747Z");

	           HttpEntity<String> entity = new HttpEntity<>(headers);
	           ResponseEntity<byte[]> response = restTemplate.build()
	                                                         .exchange("https://test-mathi.s3.ap-south-1.amazonaws.com/DSC_0115.JPG", HttpMethod.GET, entity, byte[].class);
	           Files.write(Paths.get("C:\\Users\\Mathi\\Downloads"), response.getBody());
	       
	       }catch (Exception e){
	           e.printStackTrace();
	       }
	   }
}
