package com.demo.controllers.admin;

import com.demo.DTO.OrderDTO;
import com.demo.DTO.OrderItemDTO;
import com.demo.models.Order;
import com.demo.service.OrderItemService;
import com.demo.service.OrderService;
import com.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/orders")
public class OrderController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private OrderItemService orderItemService;

    @GetMapping("")
    public String getOrders(Model model, HttpSession session) {
    	
    	List<OrderDTO> orderDTOs = orderService.getAllOrders().stream()
    			.map(e -> e.toDTO()).collect(Collectors.toList());

    	model.addAttribute("orderDTOs", orderDTOs);
    	
        return "/admin/order/orders";
    }
    
    @GetMapping("/order-item/{id}")
	public String listItemOfOrder (Model model, 
			@PathVariable(name = "id") Long id) {
		
    	Order order = orderService.getOrderById(id);
    	
		List<OrderItemDTO> orderItemDTOs = orderItemService.getByOrder(order).stream()
				.map(e -> e.toDTO()).collect(Collectors.toList());
		
		model.addAttribute("orderDTO", order.toDTO());
		model.addAttribute("orderItemDTOs", orderItemDTOs);
		
		return "/admin/order/items";
	}
    
    @GetMapping("/delete-order/{id}")
	public String deleteOrder (Model model, 
			@PathVariable(name = "id") Long id) {
		
    	Order order = orderService.getOrderById(id);
    	
		orderItemService.deleteOrder(order);
		
		return "redirect:/admin/orders";
	}
}
