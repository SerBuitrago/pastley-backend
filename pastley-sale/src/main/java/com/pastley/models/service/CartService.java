package com.pastley.models.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pastley.util.PastleyDate;
import com.pastley.util.PastleyInterface;
import com.pastley.util.PastleyValidate;
import com.pastley.util.exception.PastleyException;
import com.pastley.models.entity.Cart;
import com.pastley.models.model.ProductModel;
import com.pastley.models.repository.CartRepository;

/**
 * @project Pastley-Sale.
 * @author Sergio Stives Barrios Buitrago.
 * @Github https://github.com/SerBuitrago.
 * @contributors leynerjoseoa.
 * @version 1.0.0.
 */
@Service
public class CartService implements PastleyInterface<Long, Cart> {

	private static final Logger LOGGER = LoggerFactory.getLogger(CartService.class);

	@Autowired
	CartRepository cartRepository;
	@Autowired
	SaleService saleService;

	@Transactional(readOnly = true)
	@Override
	public Cart findById(Long id) {
		if (id <= 0)
			throw new PastleyException(HttpStatus.NOT_FOUND, "El id del producto del carrito no es valido.");
		Optional<Cart> cart = cartRepository.findById(id);
		if (!cart.isPresent())
			throw new PastleyException(HttpStatus.NOT_FOUND,
					"No se ha encontrado ningun producto en el carrito con el id " + id + ".");
		cart.get().calculate();
		return cart.orElse(null);
	}
	
	@Transactional(readOnly = true)
	public Cart findByCustomerAndProductAndStatu(boolean statu, Long idCustomer, Long idProduct) {
		testCustomer(idCustomer);
		testProduct(idProduct);
		Cart cart = cartRepository.findByCustomerAndProductAndStatu(statu, idCustomer, idProduct);
		if (cart == null)
			throw new PastleyException(HttpStatus.NOT_FOUND,
					"No se ha encontrado ningun producto de carrito con el id del cliente " + idCustomer
							+ ", id producto " + idProduct + " y estado " + statu + ".");
		return cart;
	}

	@Transactional(readOnly = true)
	@Override
	public List<Cart> findAll() {
		return calculate(cartRepository.findAll());
	}

	@Transactional(readOnly = true)
	@Override
	public List<Cart> findByStatuAll(boolean statu) {
		return calculate(cartRepository.findByStatu(statu));
	}

	@Transactional(readOnly = true)
	public List<Cart> findByCustomer(Long idCustomer) {
		testCustomer(idCustomer);
		return calculate(cartRepository.findByIdCustomer(idCustomer));
	}

	public List<Cart> findByCustomerAndStatus(Long idCustomer, boolean statu) {
		testCustomer(idCustomer);
		return calculate(cartRepository.findByCustomerAndStatus(idCustomer, statu));
	}

	public List<Cart> findByCustomerAndProduct(Long idCustomer, Long idProduct) {
		testCustomer(idCustomer);
		testProduct(idProduct);
		return calculate(cartRepository.findByCustomerAndProduct(idCustomer, idProduct));
	}

	public List<Cart> findByProductAndStatus(Long idProduct, boolean statu) {
		saleService.findProductById(idProduct);
		return calculate(cartRepository.findByProductAndStatus(idProduct, statu));
	}

	public List<Cart> findByRangeDateRegister(String start, String end) {
		String array_date[] = PastleyValidate.isRangeDateRegisterValidateDate(start, end);
		return calculate(cartRepository.findByRangeDateRegister(array_date[0], array_date[1]));
	}

	public List<Cart> findByRangeDateRegisterAndCustomer(Long idCustomer, String start, String end) {
		String array_date[] = PastleyValidate.isRangeDateRegisterValidateDate(start, end);
		return calculate(cartRepository.findByRangeDateRegisterAndCustomer(idCustomer, array_date[0], array_date[1]));
	}
	
	@Override
	public Cart save(Cart entity) {
		return null;
	}

	public Cart save(Cart entity, int type) {
		if (entity == null)
			throw new PastleyException(HttpStatus.NOT_FOUND, "No se ha recibido el cart.");
		String message = entity.validate(false), messageType = saveToMessage(type);
		if (message != null)
			throw new PastleyException(HttpStatus.NOT_FOUND,
					"No se ha " + messageType + " el producto carrito, " + message);
		ProductModel product = saleService.findProductById(entity.getIdProduct());
		Cart cart = (entity.getId() != null && entity.getId() > 0) ? saveToUpdate(entity, type)
				: saveToSave(entity, type);
		cart.setPrice(PastleyValidate.bigIntegerHigherZero(entity.getPrice()) ? entity.getPrice()
				: PastleyValidate.bigIntegerHigherZero(product.getPrice()) ? product.getPrice() : BigInteger.ZERO);
		if (!PastleyValidate.bigIntegerHigherZero(cart.getPrice()))
			throw new PastleyException(HttpStatus.NOT_FOUND,
					"No se ha " + messageType + " el producto carrito, el precio del producto debe ser mayor a cero.");
		cart.setDiscount(PastleyValidate.isChain(cart.getDiscount()) ? cart.getDiscount()
				: PastleyValidate.isChain(product.getDiscount()) ? product.getDiscount() : "0");
		cart.setVat(PastleyValidate.isChain(cart.getVat()) ? cart.getVat()
				: PastleyValidate.isChain(product.getVat()) ? product.getVat() : "0");
		cart.calculate();
		cart = cartRepository.save(cart);
		if (cart == null)
			throw new PastleyException(HttpStatus.NOT_FOUND, "No se ha " + messageType + " el producto carrito.");
		cart.calculate();
		return cart;
	}

	@Override
	public boolean delete(Long id) {
		Cart cart = findById(id);
		if (!cart.isStatu())
			throw new PastleyException(HttpStatus.NOT_FOUND,
					"No se ha eliminado el producto del carito con el id " + id + ", ya se realizo la venta.");
		cartRepository.deleteById(id);
		try {
			if (findById(id) == null)
				return true;
		} catch (PastleyException e) {
			LOGGER.error("[delete(Long id)]", e);
			return true;
		}
		throw new PastleyException(HttpStatus.NOT_FOUND,
				"No se ha eliminado el producto del carito con el id " + id + ".");
	}

	private Cart saveToSave(Cart entity, int type) {
		try {
			findByCustomerAndProductAndStatu(true, entity.getIdCustomer(), entity.getIdProduct());
		} catch (Exception e) {
			LOGGER.error("[saveToSave(Cart entity, int type)]", e);
			PastleyDate date = new PastleyDate();
			entity.setId(0L);
			entity.setDateRegister(date.currentToDateTime(null));
			entity.setDateUpdate(null);
			entity.setStatu(true);
			entity.setCount(entity.getCount() <= 0 ? 1 : entity.getCount());
			return entity;
		}
		throw new PastleyException(HttpStatus.NOT_FOUND, "El cliente id " + entity.getIdCustomer()
				+ " ya tiene agregado en el carrito el producto con el id " + entity.getIdProduct() + ".");
	}

	private Cart saveToUpdate(Cart entity, int type) {
		Cart cart = findById(entity.getId());
		if (cart == null)
			throw new PastleyException(HttpStatus.NOT_FOUND,
					"No se ha encontrado ningun producto carrito con el id " + entity.getId() + ".");
		PastleyDate date = new PastleyDate();
		entity.setDateRegister(cart.getDateRegister());
		entity.setCount((type == 4) ? entity.getCount()
				: (cart.getCount() <= 0) ? ((cart.getCount() <= 0) ? 1 : cart.getCount()) : entity.getCount());
		entity.setStatu((type == 4) ? cart.isStatu() : (type == 3) ? !entity.isStatu() : entity.isStatu());
		entity.setDateUpdate(date.currentToDateTime(null));
		return entity;
	}

	private List<Cart> calculate(List<Cart> list) {
		if (!list.isEmpty())
			list.forEach((e) -> {
				e.calculate();
			});
		return list;
	}

	private String saveToMessage(int type) {
		String message = null;
		switch (type) {
		case 1:
			message = "registrado";
			break;
		case 2:
			message = "actualizado";
			break;
		case 3:
			message = "actualizado el estado";
			break;
		case 4:
			message = "actualizando cantidad";
			break;
		default:
			message = "n/a";
			break;
		}
		return message;
	}

	private void testCustomer(Long idCustomer) {
		if (idCustomer <= 0)
			throw new PastleyException(HttpStatus.NOT_FOUND, "El id del cliente no es valido.");
	}

	private void testProduct(Long idProduct) {
		if (idProduct <= 0)
			throw new PastleyException(HttpStatus.NOT_FOUND, "El id del producto no es valido.");
	}
}
