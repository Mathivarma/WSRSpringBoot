package com.mathi.ControllerWSRCount;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
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
}
