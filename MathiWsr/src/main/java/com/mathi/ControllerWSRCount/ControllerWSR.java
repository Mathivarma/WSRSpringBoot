package com.mathi.ControllerWSRCount;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class ControllerWSR {
	@PostMapping("/WsrCount")
	public String WRSCount(@RequestBody  String oo) {
		System.out.println(oo.toString());
	//	String word="-hbgUS343354 - jhdjkhekfhjsdfjkhjkkk.\n //-US343359-jkhsdfjsdlf. \n\\DE893748 -fewfjkasd. //\nDEU3433544-jkshdfjds.US456456-wefwefwewsfd.DE3626172-sdfsdfsdf.";
		String word=oo;
		String[] ans=word.split("((?<![0-9]))[^US\\d*|^DE\\d*]");

		List<String> l = Arrays.asList(ans);
		l=l.stream().filter(i->i.length()>0).distinct().collect(Collectors.toList());
l.forEach(System.out::println);
	List<String> US=l.stream().filter(a->a.matches("^US.*")).collect(Collectors.toList());
	List<String> DE=l.stream().filter(a->a.matches("^DE.*")).collect(Collectors.toList());
	System.out.println(String.valueOf(l.size())+String.valueOf(US.size())+String.valueOf(DE.size()));
		return String.valueOf(l.size())+String.valueOf(US.size())+String.valueOf(DE.size());
	}
}
