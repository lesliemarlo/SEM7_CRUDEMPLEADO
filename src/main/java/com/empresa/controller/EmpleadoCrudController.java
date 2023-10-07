package com.empresa.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.empresa.entity.Empleado;
import com.empresa.service.EmpleadoService;
import com.empresa.util.FunctionUtil;

@Controller

public class EmpleadoCrudController {
	
	@Autowired
	private EmpleadoService empleadoService;
	
	@GetMapping(value = "/verCrudEmpleado")
	public String verEmpleado()  {return "crudEmpleado";}
	
	//luego de añadir el javascript agregar el mismo url
	@GetMapping("/consultaCrudEmpleado")
	@ResponseBody
	public List<Empleado> consulta(String filtro){
		return empleadoService.listaPorNombreApellidoLike("%"+filtro+"%");
	}
		
		//BUSCAR A MAYORES DE DAD
	@ResponseBody
		@GetMapping("/buscaEmpleadoMayorEdad")
		public String busca(String fechaNacimiento) {
			
			//FUNVION A FunctionUtil
			
			if(FunctionUtil.isMayorEdad(fechaNacimiento))
			{
				return "{\"valid\":true}";
			
			}else {
				return "{\"valid\":false}";
			}
			
		}
	
	//para registrar con validaciones del mismo nom/apellido
	@PostMapping("/registraCrudEmpleado")
	@ResponseBody
	public Map<?, ?> registra(Empleado obj) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		obj.setEstado(1);
		obj.setFechaRegistro(new Date());
		obj.setFechaActualizacion(new Date());
		
		List<Empleado> lstSalida = empleadoService.
						listaPorNombreApellidoIgual(
								obj.getNombres(), 
								obj.getApellidos());
		if (!CollectionUtils.isEmpty(lstSalida)) {
			map.put("mensaje", "El empleado " + obj.getNombres() + " " + obj.getApellidos() + " ya existe");
			return map;
		}
		
		Empleado objSalida = empleadoService.insertaEmpleado(obj);
		if (objSalida == null) {
			map.put("mensaje", "Error en el registro");
		} else {
			List<Empleado> lista = empleadoService.listaPorNombreApellidoLike("%");
			map.put("lista", lista);
			map.put("mensaje", "Registro exitoso");
		}
		return map;
	}
		
		
	
}
