package Aleks.Che.gpt_service_back.service;

import Aleks.Che.gpt_service_back.model.order.Order;
import Aleks.Che.gpt_service_back.model.order.OrderProduct;
import Aleks.Che.gpt_service_back.model.order.ProductInOrder;
import Aleks.Che.gpt_service_back.model.order.Status;
import Aleks.Che.gpt_service_back.model.product.Product;
import Aleks.Che.gpt_service_back.model.user.User;
import Aleks.Che.gpt_service_back.repository.OrderRepository;
import Aleks.Che.gpt_service_back.repository.ProductRepository;
import Aleks.Che.gpt_service_back.repository.UserRepository;
import lombok.AllArgsConstructor;
import Aleks.Che.gpt_service_back.dto.OrderDTO;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final UserService userService;

    public Order createOrder(OrderDTO request) throws Exception {
        Order order = new Order();
        processUser(request.getUserId(), order);
        List<OrderProduct> orderProducts = processProducts(request, order);
        saveOrder(order, orderProducts, request.getTotalPrice());
        return order;
    }

    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        orderRepository.findAll().forEach(orders::add);
        return orders;
    }

    public List<Order> getOrdersByUserId(Long id) {
        return orderRepository.findByUserId(id);
    }

    public List<Order> getOrdersByCurrentUser() {
        User user = userRepository.findByUsername(userService.getAuthenticationInfo().getName())
                .orElseThrow(() -> new UsernameNotFoundException("Текущий пользователь не найден"));
        return orderRepository.findByUserId(user.getId());
    }


    private void processUser(Long userId, Order order) {
        Optional<User> optionalUser = userRepository.findById(userId);
        optionalUser.ifPresent(order::setUser);
    }

    private List<OrderProduct> processProducts(OrderDTO request, Order order) throws Exception {
        List<OrderProduct> orderProducts = new ArrayList<>();

        for (ProductInOrder productInOrder : request.getProducts()) {
            Optional<Product> optionalProduct = productRepository.findById(productInOrder.getId());
            Product product = optionalProduct.orElseThrow(() -> new Exception("Товар не найден"));

            int countInOrder = productInOrder.getCount();
            validateProductAvailability(product, countInOrder);
            setProductCountInWarehouse(product, countInOrder);
            OrderProduct orderProduct = createOrderProduct(product, countInOrder, order);
            orderProducts.add(orderProduct);
        }

        return orderProducts;
    }

    private void validateProductAvailability(Product product, int countInOrder) throws Exception {
        if (product.getCountInWarehouse() < countInOrder) {
            throw new Exception("На складе нет подходящего количества товара");
        }
    }

    private void setProductCountInWarehouse(Product product, int countInOrder) {
        product.setCountInWarehouse(product.getCountInWarehouse() - countInOrder);
        productRepository.save(product);
    }

    private OrderProduct createOrderProduct(Product product, int countInOrder, Order order) {
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setProduct(product);
        orderProduct.setCountInOrder(countInOrder);
        orderProduct.setOrder(order);
        return orderProduct;
    }

    private void saveOrder(Order order, List<OrderProduct> orderProducts, Integer totalPrice) {
        order.setOrderProducts(orderProducts);
        order.setTotalPrice(totalPrice);
        order.setDateRegistration(new Timestamp(System.currentTimeMillis()));
        order.setStatus(Status.PROCESSED);
        order.setOrderNumber(String.valueOf(Math.round(Math.random() * 1000000000)));
        orderRepository.save(order);
    }
}
