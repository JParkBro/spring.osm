package com.example.onlineshoppingmall.controller;

import com.example.onlineshoppingmall.domain.OrderEntity;
import com.example.onlineshoppingmall.domain.SearchPages;
import com.example.onlineshoppingmall.domain.UserEntity;
import com.example.onlineshoppingmall.dto.*;
import com.example.onlineshoppingmall.service.OrderService;
import com.example.onlineshoppingmall.service.UserPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserPageController {

    private final UserPageService userPageService;
    private final OrderService orderService;

    @GetMapping("/cart")
    public String showCartPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<CartItemDTO> cartItems = userPageService.getCartItems(authentication.getName());

        model.addAttribute("cartItems", cartItems);

        return "pages/user/cart";
    }

    @PostMapping("/order/checkout")
    public String showCheckoutPage(@RequestParam List<Long> selectedItemIds, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = userPageService.getUser(authentication.getName());
        model.addAttribute("user", user);

        List<CartItemDTO> orderItems = userPageService.getCartItems(selectedItemIds);
        model.addAttribute("orderItems", orderItems);

        return "pages/user/order/checkout";
    }

    @PostMapping("/order/direct")
    public String directCheckout(@RequestParam Long productId, @RequestParam int quantity, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = userPageService.getUser(authentication.getName());
        model.addAttribute("user", user);

        List<CartItemDTO> orderItems = userPageService.getDirectOrderItem(productId, quantity);
        model.addAttribute("orderItems", orderItems);

        return "pages/user/order/checkout";
    }

    @GetMapping("/order/complete")
    public String showCompletePage(@RequestParam String orderId, Model model, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OrderEntity order = orderService.getOrder(orderId);

        if (!order.getUserId().equals(authentication.getName())) {
            redirectAttributes.addFlashAttribute("alertMessage", "접근 권한이 없는 주문입니다.");
            return "redirect:/";
        }

        model.addAttribute("order", order);

        List<OrderCompleteItemDTO> orderItems = orderService.getOrderItems(orderId);
        model.addAttribute("orderItems", orderItems);

        return "pages/user/order/complete";
    }

    @GetMapping("/mypage/orders")
    public String showMyPage(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "3") int size,
            Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        SearchPages params = new SearchPages(page, size);
        params.addParam("userId", authentication.getName());

        Page<OrdersDTO> orderPage = orderService.getAllOrders(params.toMap());

        model.addAttribute("orders", orderPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", orderPage.getTotalPages());
        model.addAttribute("totalItems", orderPage.getTotalElements());

        int pageDisplayCount = 5;
        int startPage = Math.max(1, page - (pageDisplayCount / 2));
        int endPage = Math.min(orderPage.getTotalPages(), startPage + pageDisplayCount - 1);
        if (endPage == orderPage.getTotalPages()) {
            startPage = Math.max(1, endPage - pageDisplayCount + 1);
        }

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "pages/user/myPage/orders";
    }

    @GetMapping("/mypage/profile")
    public String showMyProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = userPageService.getUser(authentication.getName());

        model.addAttribute("user", user);

        return "pages/user/myPage/profile";
    }

    @GetMapping("/mypage/reviews")
    public String showMyReviews(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = userPageService.getUser(authentication.getName());

        return "pages/user/myPage/reviews";
    }
}
