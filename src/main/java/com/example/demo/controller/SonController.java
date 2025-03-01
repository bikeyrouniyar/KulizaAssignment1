package com.example.demo.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.beans.Son;
import com.example.demo.service.MapStateToError;
import com.example.demo.service.SonService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(path = "/api/object/son")
@Api(value = "/api/object/son", description = "Manage Son")
public class SonController {

	@Autowired
	private SonService sonService;

	@Autowired
	private MapStateToError mapStateToError;

	@RequestMapping(path = "/create", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "/create", notes = "Create Son Resource", response = Son.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "In Case Son Resources Couldn't Be Created") })
	public ResponseEntity<?> persistSonDetatils(@Valid @RequestBody Son son, BindingResult bindingResult) {
		ResponseEntity<?> errorMap = mapStateToError.mapStateToError(bindingResult);
		if (errorMap != null) {
			return errorMap;
		}
		Son sonToDB = sonService.saveSonDetails(son);
		return new ResponseEntity<Son>(sonToDB, HttpStatus.CREATED);
	}

	@RequestMapping(path = "/get/{id}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "/get", notes = "Retreive Son Resources Using Id", response = Son.class)
	public ResponseEntity<?> getSonDetailsByID(
			@ApiParam(value = "id", required = true) @PathVariable(value = "id") Long id) {
		Son son = sonService.getSonDetailsById(id);
		if (son == null) {
			return new ResponseEntity<String>("No Data Found For This Id", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Son>(son, HttpStatus.OK);
	}

	@RequestMapping(path = "/get", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value = "/get", notes = "Retreive All Son Resources", response = Son.class, responseContainer = "List")
	public ResponseEntity<?> getAllSonDetails() {
		List<Son> listOfSon = sonService.getAllSonDetails();
		if (listOfSon == null || listOfSon.size() == 0) {
			return new ResponseEntity<String>("Sorry No Data Found In The Database", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<List<Son>>(listOfSon, HttpStatus.OK);
	}

	@RequestMapping(path = "/delete/{id}", method = RequestMethod.DELETE, produces = "application/json")
	@ApiOperation(value = "/delete", notes = "Remove Son Resources Using Id")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<?> deleteSonById(
			@ApiParam(value = "id", required = true) @PathVariable(value = "id") Long id) {
		String response = sonService.deleteSonDetails(id);
		return new ResponseEntity<String>(response, HttpStatus.OK);
	}
}
