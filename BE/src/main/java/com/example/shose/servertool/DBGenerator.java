package com.example.shose.servertool;

import com.example.shose.server.entity.Account;
import com.example.shose.server.entity.AccountVoucher;
import com.example.shose.server.entity.Address;
import com.example.shose.server.entity.Bill;
import com.example.shose.server.entity.BillDetail;
import com.example.shose.server.entity.BillHistory;
import com.example.shose.server.entity.Brand;
import com.example.shose.server.entity.Category;
import com.example.shose.server.entity.Color;
import com.example.shose.server.entity.Customer;
import com.example.shose.server.entity.Image;
import com.example.shose.server.entity.Material;
import com.example.shose.server.entity.Product;
import com.example.shose.server.entity.ProductDetail;
import com.example.shose.server.entity.Promotion;
import com.example.shose.server.entity.PromotionProductDetail;
import com.example.shose.server.entity.Size;
import com.example.shose.server.entity.Sole;
import com.example.shose.server.entity.User;
import com.example.shose.server.entity.Voucher;
import com.example.shose.server.infrastructure.cloudinary.QRCodeAndCloudinary;
import com.example.shose.server.infrastructure.constant.GenderProductDetail;
import com.example.shose.server.infrastructure.constant.Roles;
import com.example.shose.server.infrastructure.constant.Status;
import com.example.shose.server.infrastructure.constant.StatusBill;
import com.example.shose.server.infrastructure.constant.TypeBill;
import com.example.shose.server.repository.AccountRepository;
import com.example.shose.server.repository.AccountVoucherRepository;
import com.example.shose.server.repository.AddressRepository;
import com.example.shose.server.repository.BillDetailRepository;
import com.example.shose.server.repository.BillHistoryRepository;
import com.example.shose.server.repository.BillRepository;
import com.example.shose.server.repository.BrandRepository;
import com.example.shose.server.repository.CartDetailRepository;
import com.example.shose.server.repository.CartRepository;
import com.example.shose.server.repository.CategoryRepository;
import com.example.shose.server.repository.ColorRepository;
import com.example.shose.server.repository.CustomerRepository;
import com.example.shose.server.repository.ImageRepository;
import com.example.shose.server.repository.MaterialRepository;
import com.example.shose.server.repository.NotificationRepository;
import com.example.shose.server.repository.PaymentsMethodRepository;
import com.example.shose.server.repository.ProductDetailRepository;
import com.example.shose.server.repository.ProductRepository;
import com.example.shose.server.repository.PromotionProductDetailRepository;
import com.example.shose.server.repository.PromotionRepository;
import com.example.shose.server.repository.SizeRepository;
import com.example.shose.server.repository.SoleRepository;
import com.example.shose.server.repository.UserReposiory;
import com.example.shose.server.repository.VoucherDetailRepository;
import com.example.shose.server.repository.VoucherRepository;
import com.example.shose.server.util.ConvertDateToLong;
import com.example.shose.server.util.RandomNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.math.BigDecimal;


@SpringBootApplication
@EnableJpaRepositories(
        basePackages = "com.example.shose.server.repository")
public class DBGenerator implements CommandLineRunner {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountVoucherRepository accountVoucherRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private BillHistoryRepository billHistoryRepository;

    @Autowired
    private BillDetailRepository billDetailRepository;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartDetailRepository cartDetailRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ColorRepository colorRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private PaymentsMethodRepository paymentsMethodRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductDetailRepository productDetailRepository;

    @Autowired
    private SizeRepository sizeRepository;

    @Autowired
    private SoleRepository soleRepository;

    @Autowired
    private VoucherRepository voucherRepository;
    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private VoucherDetailRepository voucherDetailRepository;

    @Autowired
    private UserReposiory userReposiory;


    @Autowired
    private PromotionProductDetailRepository promotionProductDetailRepository;

    @Override
    public void run(String... args) throws Exception {

        Category category = Category.builder().name("Giày Sneaker Low – top").status(Status.DANG_SU_DUNG).build();
        Category category1 = Category.builder().name("Sneaker nam nữ Mid – top").status(Status.DANG_SU_DUNG).build();
        Category category2 = Category.builder().name("Giày Sneaker Mule").status(Status.DANG_SU_DUNG).build();
        Category category3 = Category.builder().name("Giày Slip-on sneaker").status(Status.DANG_SU_DUNG).build();
        Category category4 = Category.builder().name("Sneaker High – top").status(Status.DANG_SU_DUNG).build();
        categoryRepository.save(category);
        categoryRepository.save(category1);
        categoryRepository.save(category2);
        categoryRepository.save(category3);
        categoryRepository.save(category4);


        Material material = Material.builder().name("Da").status(Status.DANG_SU_DUNG).build();
        Material material1 = Material.builder().name("Thêu").status(Status.DANG_SU_DUNG).build();
        Material material2 = Material.builder().name("Tổng hợp").status(Status.DANG_SU_DUNG).build();
        Material material3 = Material.builder().name("Bọt").status(Status.DANG_SU_DUNG).build();
        Material material4 = Material.builder().name("Lưới").status(Status.DANG_SU_DUNG).build();
        materialRepository.save(material);
        materialRepository.save(material1);
        materialRepository.save(material2);
        materialRepository.save(material3);
        materialRepository.save(material4);

        Brand brand = Brand.builder().name("Adidas").status(Status.DANG_SU_DUNG).build();
        Brand brand1 = Brand.builder().name("Puma").status(Status.DANG_SU_DUNG).build();
        Brand brand2 = Brand.builder().name("Gucci").status(Status.DANG_SU_DUNG).build();
        Brand brand3 = Brand.builder().name("Converse").status(Status.DANG_SU_DUNG).build();
        Brand brand4 = Brand.builder().name("Vans").status(Status.DANG_SU_DUNG).build();
        Brand brand5 = Brand.builder().name("Balenciaga").status(Status.DANG_SU_DUNG).build();
        Brand brand6 = Brand.builder().name("Reebok").status(Status.DANG_SU_DUNG).build();
        brandRepository.save(brand);
        brandRepository.save(brand1);
        brandRepository.save(brand2);
        brandRepository.save(brand3);
        brandRepository.save(brand4);
        brandRepository.save(brand5);
        brandRepository.save(brand6);

        Color color = Color.builder().code("#800000").name("Maroon").status(Status.DANG_SU_DUNG).build();
        Color color1 = Color.builder().code("#800080").name("Purple").status(Status.DANG_SU_DUNG).build();
        Color color2 = Color.builder().code("#C0C0C0").name("Silver").status(Status.DANG_SU_DUNG).build();
        Color color3 = Color.builder().code("#FAFAD2").name("LightGoldenrodYellow").status(Status.DANG_SU_DUNG).build();
        Color color4 = Color.builder().code("#FDF5E6").name("OldLace").status(Status.DANG_SU_DUNG).build();
        Color color5 = Color.builder().code("#7B68EE").name("MediumSlateBlue").status(Status.DANG_SU_DUNG).build();
        Color color6 = Color.builder().code("#87CEEB").name("SkyBlue").status(Status.DANG_SU_DUNG).build();
        Color color7 = Color.builder().code("#8B814C").name("LightGoldenrod4").status(Status.DANG_SU_DUNG).build();
        colorRepository.save(color);
        colorRepository.save(color1);
        colorRepository.save(color2);
        colorRepository.save(color3);
        colorRepository.save(color4);
        colorRepository.save(color5);
        colorRepository.save(color6);
        colorRepository.save(color7);

        Product product1 = Product.builder().code(new RandomNumberGenerator().randomToString("SP", 1500000000)).name("ECCO ATH-1FW").status(Status.DANG_SU_DUNG).build();
        Product product2 = Product.builder().code(new RandomNumberGenerator().randomToString("SP", 1500000000)).name("ECCO ASTIR LITE M").status(Status.DANG_SU_DUNG).build();
        Product product3 = Product.builder().code(new RandomNumberGenerator().randomToString("SP", 1500000000)).name("ECCO 2ND COZMO W").status(Status.DANG_SU_DUNG).build();
        Product product4 = Product.builder().code(new RandomNumberGenerator().randomToString("SP", 1500000000)).name("ECCO 2ND COZMO LLL").status(Status.DANG_SU_DUNG).build();
        Product product5 = Product.builder().code(new RandomNumberGenerator().randomToString("SP", 1500000000)).name("ECCO ULT-TRN M").status(Status.DANG_SU_DUNG).build();
        Product product6 = Product.builder().code(new RandomNumberGenerator().randomToString("SP", 1500000000)).name("ECCO MX M").status(Status.DANG_SU_DUNG).build();
        Product product7 = Product.builder().code(new RandomNumberGenerator().randomToString("SP", 1500000000)).name("ECCO ST1 LITE M").status(Status.DANG_SU_DUNG).build();
        Product product8 = Product.builder().code(new RandomNumberGenerator().randomToString("SP", 1500000000)).name("ECCO STREET LITE M").status(Status.DANG_SU_DUNG).build();
        Product product9 = Product.builder().code(new RandomNumberGenerator().randomToString("SP", 1500000000)).name("Giày Sneakers Nam NEW04 - BSN028").status(Status.DANG_SU_DUNG).build();
        Product product10 = Product.builder().code(new RandomNumberGenerator().randomToString("SP", 1500000000)).name("Giày Sneakers Nữ NEW03 - BSN027").status(Status.DANG_SU_DUNG).build();
        Product product11 = Product.builder().code(new RandomNumberGenerator().randomToString("SP", 1500000000)).name("Giày Sneakers Nam NEW01 - BSN025").status(Status.DANG_SU_DUNG).build();
        Product product12 = Product.builder().code(new RandomNumberGenerator().randomToString("SP", 1500000000)).name("Giày Sneakers Nam BELSPORTS 113 - BSN021").status(Status.DANG_SU_DUNG).build();
        Product product13 = Product.builder().code(new RandomNumberGenerator().randomToString("SP", 1500000000)).name("Giày Sneakers Nam BELSPORTS HJ06 - BSN020").status(Status.DANG_SU_DUNG).build();
        Product product14 = Product.builder().code(new RandomNumberGenerator().randomToString("SP", 1500000000)).name("Giày Sneakers Nam BELSPORTS 808-BSN006").status(Status.DANG_SU_DUNG).build();
        Product product15 = Product.builder().code(new RandomNumberGenerator().randomToString("SP", 1500000000)).name("Giày Sneakers Nữ BELSPORTS 5827-BSN007").status(Status.DANG_SU_DUNG).build();
        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);
        productRepository.save(product4);
        productRepository.save(product5);
        productRepository.save(product6);
        productRepository.save(product7);
        productRepository.save(product8);
        productRepository.save(product9);
        productRepository.save(product10);
        productRepository.save(product11);
        productRepository.save(product12);
        productRepository.save(product13);
        productRepository.save(product14);
        productRepository.save(product15);

        Sole sole1 = Sole.builder().name("Low-top").status(Status.DANG_SU_DUNG).build();
        Sole sole2 = Sole.builder().name("Mid-top").status(Status.DANG_SU_DUNG).build();
        Sole sole3 = Sole.builder().name("High-top").status(Status.DANG_SU_DUNG).build();
        Sole sole4 = Sole.builder().name("Slip-top").status(Status.DANG_SU_DUNG).build();
        soleRepository.save(sole1);
        soleRepository.save(sole2);
        soleRepository.save(sole3);
        soleRepository.save(sole4);

        Size size = Size.builder().name(44).status(Status.DANG_SU_DUNG).build();
        Size size1 = Size.builder().name(43).status(Status.DANG_SU_DUNG).build();
        Size size2 = Size.builder().name(42).status(Status.DANG_SU_DUNG).build();
        Size size3 = Size.builder().name(41).status(Status.DANG_SU_DUNG).build();
        Size size4 = Size.builder().name(40).status(Status.DANG_SU_DUNG).build();
        Size size5 = Size.builder().name(39).status(Status.DANG_SU_DUNG).build();
        Size size6 = Size.builder().name(38).status(Status.DANG_SU_DUNG).build();
        Size size7 = Size.builder().name(37).status(Status.DANG_SU_DUNG).build();
        Size size8 = Size.builder().name(36).status(Status.DANG_SU_DUNG).build();
        sizeRepository.save(size);
        sizeRepository.save(size1);
        sizeRepository.save(size2);
        sizeRepository.save(size3);
        sizeRepository.save(size4);
        sizeRepository.save(size5);
        sizeRepository.save(size6);
        sizeRepository.save(size7);
        sizeRepository.save(size8);

        ProductDetail productDetail1 = ProductDetail.builder().size(size).color(color).quantity(15)
                .sole(sole1).category(category).material(material).brand(brand).product(product1)
                .gender(GenderProductDetail.NU).price(new BigDecimal("1900001")).status(Status.DANG_SU_DUNG)
                .description("Thiết kế tối giản thanh lịch cùng form dáng ôm trọn chân lấy màu trắng là chủ đạo, tự tin phối mội loại thời trang, cho dù quân dày hay ngắn đề có thể phù hợp không cần đắng đo")
                .build();
        ProductDetail productDetail2 = ProductDetail.builder().size(size2).color(color2).quantity(15)
                .sole(sole2).category(category2).material(material2).brand(brand2).product(product1)
                .gender(GenderProductDetail.NAM).price(new BigDecimal("1900002")).status(Status.DANG_SU_DUNG)
                .description("Thiết kế tối giản thanh lịch cùng form dáng ôm trọn chân lấy màu trắng là chủ đạo, tự tin phối mội loại thời trang, cho dù quân dày hay ngắn đề có thể phù hợp không cần đắng đo")
                .build();
        ProductDetail productDetail3 = ProductDetail.builder().size(size5).color(color1).quantity(15)
                .sole(sole1).category(category).material(material).brand(brand).product(product1)
                .gender(GenderProductDetail.NAM_VA_NU).price(new BigDecimal("1900003")).status(Status.DANG_SU_DUNG)
                .description("Thiết kế tối giản thanh lịch cùng form dáng ôm trọn chân lấy màu trắng là chủ đạo, tự tin phối mội loại thời trang, cho dù quân dày hay ngắn đề có thể phù hợp không cần đắng đo")
                .build();
        ProductDetail productDetail4 = ProductDetail.builder().size(size3).color(color5).quantity(15)
                .sole(sole4).category(category3).material(material3).brand(brand).product(product1)
                .gender(GenderProductDetail.NU).price(new BigDecimal("1900004")).status(Status.DANG_SU_DUNG)
                .description("Thiết kế tối giản thanh lịch cùng form dáng ôm trọn chân lấy màu trắng là chủ đạo, tự tin phối mội loại thời trang, cho dù quân dày hay ngắn đề có thể phù hợp không cần đắng đo")
                .build();
        ProductDetail productDetail5 = ProductDetail.builder().size(size7).color(color7).quantity(15)
                .sole(sole3).category(category4).material(material2).brand(brand).product(product1)
                .gender(GenderProductDetail.NAM_VA_NU).price(new BigDecimal("1900005")).status(Status.DANG_SU_DUNG)
                .description("Thiết kế tối giản thanh lịch cùng form dáng ôm trọn chân lấy màu trắng là chủ đạo, tự tin phối mội loại thời trang, cho dù quân dày hay ngắn đề có thể phù hợp không cần đắng đo")
                .build();
        ProductDetail productDetail6 = ProductDetail.builder().size(size2).color(color4).quantity(15)
                .sole(sole1).category(category).material(material).brand(brand).product(product1)
                .gender(GenderProductDetail.NAM).price(new BigDecimal("1900006")).status(Status.DANG_SU_DUNG)
                .description("Thiết kế tối giản thanh lịch cùng form dáng ôm trọn chân lấy màu trắng là chủ đạo, tự tin phối mội loại thời trang, cho dù quân dày hay ngắn đề có thể phù hợp không cần đắng đo")
                .build();
        ProductDetail productDetail7 = ProductDetail.builder().size(size6).color(color6).quantity(15)
                .sole(sole1).category(category).material(material).brand(brand).product(product1)
                .gender(GenderProductDetail.NAM).price(new BigDecimal("1900007")).status(Status.DANG_SU_DUNG)
                .description("Thiết kế tối giản thanh lịch cùng form dáng ôm trọn chân lấy màu trắng là chủ đạo, tự tin phối mội loại thời trang, cho dù quân dày hay ngắn đề có thể phù hợp không cần đắng đo")
                .build();
        ProductDetail productDetail8 = ProductDetail.builder().size(size8).color(color6).quantity(15)
                .sole(sole1).category(category).material(material).brand(brand).product(product1)
                .gender(GenderProductDetail.NAM).price(new BigDecimal("1900008")).status(Status.DANG_SU_DUNG)
                .description("Thiết kế tối giản thanh lịch cùng form dáng ôm trọn chân lấy màu trắng là chủ đạo, tự tin phối mội loại thời trang, cho dù quân dày hay ngắn đề có thể phù hợp không cần đắng đo")
                .build();
        ProductDetail productDetail9 = ProductDetail.builder().size(size2).color(color).quantity(15)
                .sole(sole1).category(category).material(material).brand(brand).product(product1)
                .gender(GenderProductDetail.NAM_VA_NU).price(new BigDecimal("1900009")).status(Status.DANG_SU_DUNG)
                .description("Thiết kế tối giản thanh lịch cùng form dáng ôm trọn chân lấy màu trắng là chủ đạo, tự tin phối mội loại thời trang, cho dù quân dày hay ngắn đề có thể phù hợp không cần đắng đo")
                .build();
        ProductDetail productDetail10 = ProductDetail.builder().size(size8).color(color7).quantity(20)
                .sole(sole1).category(category).material(material).brand(brand).product(product2)
                .gender(GenderProductDetail.NU).price(new BigDecimal("2000000")).status(Status.DANG_SU_DUNG)
                .description("Thiết kế tối giản thanh lịch cùng form dáng ôm trọn chân lấy màu trắng là chủ đạo, tự tin phối mội loại thời trang, cho dù quân dày hay ngắn đề có thể phù hợp không cần đắng đo")
                .build();
        ProductDetail productDetail11 = ProductDetail.builder().size(size).color(color1).quantity(15)
                .sole(sole1).category(category).material(material).brand(brand).product(product2)
                .gender(GenderProductDetail.NU).price(new BigDecimal("2000000")).status(Status.DANG_SU_DUNG)
                .description("Thiết kế tối giản thanh lịch cùng form dáng ôm trọn chân lấy màu trắng là chủ đạo, tự tin phối mội loại thời trang, cho dù quân dày hay ngắn đề có thể phù hợp không cần đắng đo")
                .build();
        ProductDetail productDetail12 = ProductDetail.builder().size(size).color(color7).quantity(15)
                .sole(sole1).category(category).material(material).brand(brand).product(product3)
                .gender(GenderProductDetail.NU).price(new BigDecimal("4000000")).status(Status.DANG_SU_DUNG)
                .description("Thiết kế tối giản thanh lịch cùng form dáng ôm trọn chân lấy màu trắng là chủ đạo, tự tin phối mội loại thời trang, cho dù quân dày hay ngắn đề có thể phù hợp không cần đắng đo")
                .build();
        productDetailRepository.save(productDetail1);
        productDetailRepository.save(productDetail2);
        productDetailRepository.save(productDetail3);
        productDetailRepository.save(productDetail4);
        productDetailRepository.save(productDetail5);
        productDetailRepository.save(productDetail6);
        productDetailRepository.save(productDetail7);
        productDetailRepository.save(productDetail8);
        productDetailRepository.save(productDetail9);
        productDetailRepository.save(productDetail10);
        productDetailRepository.save(productDetail11);
        productDetailRepository.save(productDetail12);


        //image
        Image image1 = Image.builder().name("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR5ZtafZviyMHQqvzaWG-HYYmsEqQM51r5A3Q&usqp=CAU").productDetail(productDetail1).status(true).build();
        Image image2 = Image.builder().name("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR5ZtafZviyMHQqvzaWG-HYYmsEqQM51r5A3Q&usqp=CAU").productDetail(productDetail2).status(true).build();
        Image image3 = Image.builder().name("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRa862z-avbTOzB2dRIfSWwt1-qkYp01v91gA&usqp=CAU").productDetail(productDetail3).status(true).build();
        Image image4 = Image.builder().name("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRa862z-avbTOzB2dRIfSWwt1-qkYp01v91gA&usqp=CAU").productDetail(productDetail4).status(true).build();
        Image image5 = Image.builder().name("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRa862z-avbTOzB2dRIfSWwt1-qkYp01v91gA&usqp=CAU").productDetail(productDetail5).status(true).build();
        Image image6 = Image.builder().name("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR5ZtafZviyMHQqvzaWG-HYYmsEqQM51r5A3Q&usqp=CAU").productDetail(productDetail6).status(true).build();
        Image image7 = Image.builder().name("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR5ZtafZviyMHQqvzaWG-HYYmsEqQM51r5A3Q&usqp=CAU").productDetail(productDetail7).status(true).build();
        Image image8 = Image.builder().name("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT1t1oEA4212-3N-gMoUhHK2tVcJRICAWexdQ&usqp=CAU").productDetail(productDetail8).status(true).build();
        Image image9 = Image.builder().name("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR5ZtafZviyMHQqvzaWG-HYYmsEqQM51r5A3Q&usqp=CAU").productDetail(productDetail9).status(true).build();
        Image image10 = Image.builder().name("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT1t1oEA4212-3N-gMoUhHK2tVcJRICAWexdQ&usqp=CAU").productDetail(productDetail10).status(true).build();
        Image image11 = Image.builder().name("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT1t1oEA4212-3N-gMoUhHK2tVcJRICAWexdQ&usqp=CAU").productDetail(productDetail11).status(true).build();
        Image image12 = Image.builder().name("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT1t1oEA4212-3N-gMoUhHK2tVcJRICAWexdQ&usqp=CAU").productDetail(productDetail12).status(true).build();
        imageRepository.save(image1);
        imageRepository.save(image2);
        imageRepository.save(image3);
        imageRepository.save(image4);
        imageRepository.save(image5);
        imageRepository.save(image6);
        imageRepository.save(image7);
        imageRepository.save(image8);
        imageRepository.save(image9);
        imageRepository.save(image10);
        imageRepository.save(image11);
        imageRepository.save(image12);

        User user1 = User.builder()
                .avata("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTaIP8Lvis7vhRKgCFKfPoWTxrNE4HnQ18gdg&usqp=CAU")
                .fullName("Nguyễn Văn Vinh").dateOfBirth(new ConvertDateToLong().dateToLong("01/06/2000")).email("vinhnvph23845@fpt.edu.vn")
                .gender(true).phoneNumber("0777989898").status(Status.DANG_SU_DUNG)
                .build();
        User user2 = User.builder()
                .avata("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR4RGwl1mqaD9sd_JO7m0KPAlgZmnClgtkEqQ&usqp=CAU")
                .fullName("Đinh Khắc Diệm").dateOfBirth(new ConvertDateToLong().dateToLong("01/06/2003")).email("diem@gmail.com")
                .gender(true).phoneNumber("066697897978").status(Status.DANG_SU_DUNG)
                .build();
        User user3 = User.builder()
                .avata("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSo1OaW3VfQMuHMJiqrUGUxoEUDI4aNVu6mWQ&usqp=CAU")
                .fullName("Dương Tu Thắng").dateOfBirth(new ConvertDateToLong().dateToLong("01/06/20000")).email("thangdt@fpt.edu.vn")
                .gender(true).phoneNumber("0888898989").status(Status.DANG_SU_DUNG)
                .build();
        userReposiory.save(user1);
        userReposiory.save(user2);
        userReposiory.save(user3);

        Account account1 = Account.builder().user(user1).email(user1.getEmail()).password("123").roles(Roles.ADMIN).build();
        Account account2 = Account.builder().user(user3).email(user2.getEmail()).password("123").roles(Roles.NHAN_VIEN).build();
        Account account3 = Account.builder().user(user2).email(user3.getEmail()).password("123").roles(Roles.USER).build();
        accountRepository.save(account1);
        accountRepository.save(account2);
        accountRepository.save(account3);

        Customer customer1 = Customer.builder().fullName("Hà Phương Na").phoneNumber("0951753852").email("phuongna@gmail.com").status(Status.DANG_SU_DUNG).build();
        customerRepository.save(customer1);

        Bill bill1 = Bill.builder().code("HD0001")
                .phoneNumber("0987654321").address("Thọ An - Đan Phượng - Hà Nội").userName("Nguyễn Văn A").itemDiscount(new BigDecimal("2000000"))
                .totalMoney(new BigDecimal("1800000")).completionDate(new ConvertDateToLong().dateToLong("11/09/2023"))
                .deliveryDate(new ConvertDateToLong().dateToLong("12/05/2023")).deliveryDate(new ConvertDateToLong().dateToLong("15/05/2023")).confirmationDate(new ConvertDateToLong().dateToLong("20/05/2023"))
                .typeBill(TypeBill.ONLINE).note("Đã hoàn thành").moneyShip(new BigDecimal("15000")).statusBill(StatusBill.CHO_XAC_NHAN)
                .employees(account2).account(account3)
                .build();

        Bill bill2 = Bill.builder().code("HD0002")
                .phoneNumber("0987654321").address("Thọ An - Đan Phượng - Hà Nội").userName("Nguyễn Văn B").itemDiscount(new BigDecimal("2000000"))
                .totalMoney(new BigDecimal("1800000")).completionDate(new ConvertDateToLong().dateToLong("12/09/2023"))
                .deliveryDate(new ConvertDateToLong().dateToLong("14/05/2023")).deliveryDate(new ConvertDateToLong().dateToLong("16/05/2023")).confirmationDate(new ConvertDateToLong().dateToLong("22/05/2023"))
                .typeBill(TypeBill.ONLINE).note("Đã hoàn thành").moneyShip(new BigDecimal("15000")).statusBill(StatusBill.THANH_CONG)
                .employees(account1).customer(customer1)
                .build();
        Bill bill3 = Bill.builder().code("HD0003")
                .phoneNumber("0987654321").address("Thọ An - Đan Phượng - Hà Nội").userName("Nguyễn Văn B").itemDiscount(new BigDecimal("2000000"))
                .totalMoney(new BigDecimal("1800000")).completionDate(new ConvertDateToLong().dateToLong("12/09/2023"))
                .deliveryDate(new ConvertDateToLong().dateToLong("14/05/2023")).deliveryDate(new ConvertDateToLong().dateToLong("16/05/2023")).confirmationDate(new ConvertDateToLong().dateToLong("22/05/2023"))
                .typeBill(TypeBill.ONLINE).note("Đã hoàn thành").moneyShip(new BigDecimal("15000")).statusBill(StatusBill.THANH_CONG)
                .employees(account1).customer(customer1)
                .build();
        Bill bill4 = Bill.builder().code("HD0004")
                .phoneNumber("0987654321").address("Thọ An - Đan Phượng - Hà Nội").userName("Nguyễn Văn B").itemDiscount(new BigDecimal("2000000"))
                .totalMoney(new BigDecimal("1800000")).completionDate(new ConvertDateToLong().dateToLong("13/08/2023"))
                .deliveryDate(new ConvertDateToLong().dateToLong("14/05/2023")).deliveryDate(new ConvertDateToLong().dateToLong("16/05/2023")).confirmationDate(new ConvertDateToLong().dateToLong("22/05/2023"))
                .typeBill(TypeBill.ONLINE).note("Đã hoàn thành").moneyShip(new BigDecimal("15000")).statusBill(StatusBill.VAN_CHUYEN)
                .employees(account1).customer(customer1)
                .build();
        Bill bill5 = Bill.builder().code("HD0005")
                .phoneNumber("0987654321").address("Thọ An - Đan Phượng - Hà Nội").userName("Nguyễn Văn B").itemDiscount(new BigDecimal("2000000"))
                .totalMoney(new BigDecimal("1800000")).completionDate(new ConvertDateToLong().dateToLong("14/08/2023"))
                .deliveryDate(new ConvertDateToLong().dateToLong("14/05/2023")).deliveryDate(new ConvertDateToLong().dateToLong("16/05/2023")).confirmationDate(new ConvertDateToLong().dateToLong("22/05/2023"))
                .typeBill(TypeBill.ONLINE).note("Đã hoàn thành").moneyShip(new BigDecimal("15000")).statusBill(StatusBill.CHO_VAN_CHUYEN)
                .employees(account1).customer(customer1)
                .build();
        Bill bill6 = Bill.builder().code("HD0006")
                .phoneNumber("0987654321").address("Thọ An - Đan Phượng - Hà Nội").userName("Nguyễn Văn B").itemDiscount(new BigDecimal("2000000"))
                .totalMoney(new BigDecimal("1800000")).completionDate(new ConvertDateToLong().dateToLong("15/08/2023"))
                .deliveryDate(new ConvertDateToLong().dateToLong("14/05/2023")).deliveryDate(new ConvertDateToLong().dateToLong("16/05/2023")).confirmationDate(new ConvertDateToLong().dateToLong("22/05/2023"))
                .typeBill(TypeBill.ONLINE).note("Đã hoàn thành").moneyShip(new BigDecimal("15000")).statusBill(StatusBill.DA_HUY)
                .employees(account1).customer(customer1)
                .build();
        Bill bill7 = Bill.builder().code("HD0007")
                .phoneNumber("0987654321").address("Thọ An - Đan Phượng - Hà Nội").userName("Nguyễn Văn B").itemDiscount(new BigDecimal("2000000"))
                .totalMoney(new BigDecimal("1800000")).completionDate(new ConvertDateToLong().dateToLong("16/08/2023"))
                .deliveryDate(new ConvertDateToLong().dateToLong("14/05/2023")).deliveryDate(new ConvertDateToLong().dateToLong("16/05/2023")).confirmationDate(new ConvertDateToLong().dateToLong("22/05/2023"))
                .typeBill(TypeBill.ONLINE).note("Đã hoàn thành").moneyShip(new BigDecimal("15000")).statusBill(StatusBill.CHO_XAC_NHAN)
                .employees(account1).customer(customer1)
                .build();
        Bill bill8 = Bill.builder().code("HD0008")
                .phoneNumber("0987654321").address("Thọ An - Đan Phượng - Hà Nội").userName("Nguyễn Văn A").itemDiscount(new BigDecimal("2000000"))
                .totalMoney(new BigDecimal("1800000")).completionDate(new ConvertDateToLong().dateToLong("16/09/2023"))
                .deliveryDate(new ConvertDateToLong().dateToLong("12/05/2023")).deliveryDate(new ConvertDateToLong().dateToLong("15/05/2023")).confirmationDate(new ConvertDateToLong().dateToLong("20/05/2023"))
                .typeBill(TypeBill.ONLINE).note("Đã hoàn thành").moneyShip(new BigDecimal("15000")).statusBill(StatusBill.THANH_CONG)
                .employees(account2).account(account3)
                .build();

        Bill bill9 = Bill.builder().code("HD0009")
                .phoneNumber("0987654321").address("Thọ An - Đan Phượng - Hà Nội").userName("Nguyễn Văn B").itemDiscount(new BigDecimal("2000000"))
                .totalMoney(new BigDecimal("1800000")).completionDate(new ConvertDateToLong().dateToLong("16/09/2023"))
                .deliveryDate(new ConvertDateToLong().dateToLong("14/05/2023")).deliveryDate(new ConvertDateToLong().dateToLong("16/05/2023")).confirmationDate(new ConvertDateToLong().dateToLong("22/05/2023"))
                .typeBill(TypeBill.ONLINE).note("Đã hoàn thành").moneyShip(new BigDecimal("15000")).statusBill(StatusBill.THANH_CONG)
                .employees(account1).customer(customer1)
                .build();
        Bill bill10 = Bill.builder().code("HD0010")
                .phoneNumber("0987654321").address("Thọ An - Đan Phượng - Hà Nội").userName("Nguyễn Văn B").itemDiscount(new BigDecimal("2000000"))
                .totalMoney(new BigDecimal("1800000")).completionDate(new ConvertDateToLong().dateToLong("17/08/2023"))
                .deliveryDate(new ConvertDateToLong().dateToLong("14/05/2023")).deliveryDate(new ConvertDateToLong().dateToLong("16/05/2023")).confirmationDate(new ConvertDateToLong().dateToLong("22/05/2023"))
                .typeBill(TypeBill.ONLINE).note("Đã hoàn thành").moneyShip(new BigDecimal("15000")).statusBill(StatusBill.DA_THANH_TOAN)
                .employees(account1).customer(customer1)
                .build();
        Bill bill11 = Bill.builder().code("HD0011")
                .phoneNumber("0987654321").address("Thọ An - Đan Phượng - Hà Nội").userName("Nguyễn Văn B").itemDiscount(new BigDecimal("2000000"))
                .totalMoney(new BigDecimal("1800000")).completionDate(new ConvertDateToLong().dateToLong("17/09/2023"))
                .deliveryDate(new ConvertDateToLong().dateToLong("14/05/2023")).deliveryDate(new ConvertDateToLong().dateToLong("16/05/2023")).confirmationDate(new ConvertDateToLong().dateToLong("22/05/2023"))
                .typeBill(TypeBill.ONLINE).note("Đã hoàn thành").moneyShip(new BigDecimal("15000")).statusBill(StatusBill.THANH_CONG)
                .employees(account1).customer(customer1)
                .build();
        Bill bill12 = Bill.builder().code("HD0012")
                .phoneNumber("0987654321").address("Thọ An - Đan Phượng - Hà Nội").userName("Nguyễn Văn B").itemDiscount(new BigDecimal("2000000"))
                .totalMoney(new BigDecimal("1800000")).completionDate(new ConvertDateToLong().dateToLong("17/09/2023"))
                .deliveryDate(new ConvertDateToLong().dateToLong("14/05/2023")).deliveryDate(new ConvertDateToLong().dateToLong("16/05/2023")).confirmationDate(new ConvertDateToLong().dateToLong("22/05/2023"))
                .typeBill(TypeBill.ONLINE).note("Đã hoàn thành").moneyShip(new BigDecimal("15000")).statusBill(StatusBill.THANH_CONG)
                .employees(account1).customer(customer1)
                .build();
        Bill bill13 = Bill.builder().code("HD0013")
                .phoneNumber("0987654321").address("Thọ An - Đan Phượng - Hà Nội").userName("Nguyễn Văn B").itemDiscount(new BigDecimal("2000000"))
                .totalMoney(new BigDecimal("1800000")).completionDate(new ConvertDateToLong().dateToLong("18/08/2023"))
                .deliveryDate(new ConvertDateToLong().dateToLong("14/05/2023")).deliveryDate(new ConvertDateToLong().dateToLong("16/05/2023")).confirmationDate(new ConvertDateToLong().dateToLong("22/05/2023"))
                .typeBill(TypeBill.ONLINE).note("Đã hoàn thành").moneyShip(new BigDecimal("15000")).statusBill(StatusBill.THANH_CONG)
                .employees(account1).customer(customer1)
                .build();
        Bill bill14 = Bill.builder().code("HD0014")
                .phoneNumber("0987654321").address("Thọ An - Đan Phượng - Hà Nội").userName("Nguyễn Văn B").itemDiscount(new BigDecimal("2000000"))
                .totalMoney(new BigDecimal("1800000")).completionDate(new ConvertDateToLong().dateToLong("19/09/2023"))
                .deliveryDate(new ConvertDateToLong().dateToLong("14/05/2023")).deliveryDate(new ConvertDateToLong().dateToLong("16/05/2023")).confirmationDate(new ConvertDateToLong().dateToLong("22/05/2023"))
                .typeBill(TypeBill.ONLINE).note("Đã hoàn thành").moneyShip(new BigDecimal("15000")).statusBill(StatusBill.DA_THANH_TOAN)
                .employees(account1).customer(customer1)
                .build();
        billRepository.save(bill1);
        billRepository.save(bill2);
        billRepository.save(bill3);
        billRepository.save(bill4);
        billRepository.save(bill5);
        billRepository.save(bill6);
        billRepository.save(bill7);
        billRepository.save(bill8);
        billRepository.save(bill9);
        billRepository.save(bill10);
        billRepository.save(bill11);
        billRepository.save(bill12);
        billRepository.save(bill13);
        billRepository.save(bill14);

        BillDetail billDetail = BillDetail.builder().bill(bill1).statusBill(StatusBill.THANH_CONG).productDetail(productDetail1).quantity(1).price(productDetail1.getPrice()).build();
        BillDetail billDetail1 = BillDetail.builder().bill(bill1).statusBill(StatusBill.THANH_CONG).productDetail(productDetail3).quantity(2).price(productDetail3.getPrice()).build();
        BillDetail billDetail2 = BillDetail.builder().bill(bill1).statusBill(StatusBill.THANH_CONG).productDetail(productDetail9).quantity(2).price(productDetail9.getPrice()).build();
        BillDetail billDetail3 = BillDetail.builder().bill(bill2).statusBill(StatusBill.THANH_CONG).productDetail(productDetail2).quantity(1).price(productDetail2.getPrice()).build();
        BillDetail billDetail4 = BillDetail.builder().bill(bill2).statusBill(StatusBill.THANH_CONG).productDetail(productDetail2).quantity(1).price(productDetail2.getPrice()).build();
        BillDetail billDetail5 = BillDetail.builder().bill(bill3).statusBill(StatusBill.THANH_CONG).productDetail(productDetail1).quantity(2).price(productDetail1.getPrice()).build();
        BillDetail billDetail6 = BillDetail.builder().bill(bill4).statusBill(StatusBill.VAN_CHUYEN).productDetail(productDetail3).quantity(3).price(productDetail3.getPrice()).build();
        BillDetail billDetail7 = BillDetail.builder().bill(bill5).statusBill(StatusBill.CHO_VAN_CHUYEN).productDetail(productDetail9).quantity(3).price(productDetail9.getPrice()).build();
        BillDetail billDetail8 = BillDetail.builder().bill(bill6).statusBill(StatusBill.DA_HUY).productDetail(productDetail2).quantity(2).price(productDetail2.getPrice()).build();
        BillDetail billDetail9 = BillDetail.builder().bill(bill7).statusBill(StatusBill.CHO_XAC_NHAN).productDetail(productDetail2).quantity(1).price(productDetail2.getPrice()).build();

        BillDetail billDetail10 = BillDetail.builder().bill(bill8).statusBill(StatusBill.THANH_CONG).productDetail(productDetail1).quantity(3).price(productDetail1.getPrice()).build();
        BillDetail billDetail11 = BillDetail.builder().bill(bill9).statusBill(StatusBill.THANH_CONG).productDetail(productDetail3).quantity(2).price(productDetail3.getPrice()).build();
        BillDetail billDetail12 = BillDetail.builder().bill(bill10).statusBill(StatusBill.THANH_CONG).productDetail(productDetail9).quantity(2).price(productDetail9.getPrice()).build();
        BillDetail billDetail13 = BillDetail.builder().bill(bill11).statusBill(StatusBill.THANH_CONG).productDetail(productDetail2).quantity(4).price(productDetail2.getPrice()).build();
        BillDetail billDetail14 = BillDetail.builder().bill(bill12).statusBill(StatusBill.THANH_CONG).productDetail(productDetail2).quantity(2).price(productDetail2.getPrice()).build();
        BillDetail billDetail15 = BillDetail.builder().bill(bill13).statusBill(StatusBill.THANH_CONG).productDetail(productDetail1).quantity(1).price(productDetail1.getPrice()).build();
        BillDetail billDetail16 = BillDetail.builder().bill(bill14).statusBill(StatusBill.THANH_CONG).productDetail(productDetail3).quantity(2).price(productDetail3.getPrice()).build();
        BillDetail billDetail17 = BillDetail.builder().bill(bill13).statusBill(StatusBill.THANH_CONG).productDetail(productDetail9).quantity(3).price(productDetail9.getPrice()).build();
        BillDetail billDetail18 = BillDetail.builder().bill(bill14).statusBill(StatusBill.THANH_CONG).productDetail(productDetail2).quantity(2).price(productDetail2.getPrice()).build();
        BillDetail billDetail19 = BillDetail.builder().bill(bill12).statusBill(StatusBill.THANH_CONG).productDetail(productDetail2).quantity(1).price(productDetail2.getPrice()).build();
        billDetailRepository.save(billDetail);
        billDetailRepository.save(billDetail1);
        billDetailRepository.save(billDetail2);
        billDetailRepository.save(billDetail3);
        billDetailRepository.save(billDetail4);
        billDetailRepository.save(billDetail5);
        billDetailRepository.save(billDetail6);
        billDetailRepository.save(billDetail7);
        billDetailRepository.save(billDetail8);
        billDetailRepository.save(billDetail9);
        billDetailRepository.save(billDetail10);
        billDetailRepository.save(billDetail11);
        billDetailRepository.save(billDetail12);
        billDetailRepository.save(billDetail13);
        billDetailRepository.save(billDetail14);
        billDetailRepository.save(billDetail15);
        billDetailRepository.save(billDetail16);
        billDetailRepository.save(billDetail17);
        billDetailRepository.save(billDetail18);
        billDetailRepository.save(billDetail19);

        BillHistory billHistory = BillHistory.builder().statusBill(StatusBill.TAO_HOA_DON).bill(bill1)
                .build();
        BillHistory billHistory1 = BillHistory.builder().statusBill(StatusBill.CHO_XAC_NHAN).bill(bill1)
                .build();
        BillHistory billHistory2 = BillHistory.builder().statusBill(StatusBill.VAN_CHUYEN).bill(bill1)
                .build();
        BillHistory billHistory3 = BillHistory.builder().statusBill(StatusBill.DA_THANH_TOAN).bill(bill1)
                .build();
        BillHistory billHistory5 = BillHistory.builder().statusBill(StatusBill.TAO_HOA_DON).bill(bill2)
                .build();
        BillHistory billHistory6 = BillHistory.builder().statusBill(StatusBill.CHO_XAC_NHAN).bill(bill2)
                .build();
        BillHistory billHistory7 = BillHistory.builder().statusBill(StatusBill.DA_HUY).bill(bill2)
                .build();
        billHistoryRepository.save(billHistory);
        billHistoryRepository.save(billHistory1);
        billHistoryRepository.save(billHistory2);
        billHistoryRepository.save(billHistory3);
        billHistoryRepository.save(billHistory5);
        billHistoryRepository.save(billHistory6);
        billHistoryRepository.save(billHistory7);
        Voucher voucher1 = Voucher.builder().code(new RandomNumberGenerator().randomToString("VC", 5))
                .name("Sale ngày khai trương").value(new BigDecimal(100000))
                .startDate(new ConvertDateToLong().dateToLong("25/05/2023")).endDate(new ConvertDateToLong().dateToLong("01/06/2023"))
                .quantity(100).status(Status.DANG_SU_DUNG).build();
        Voucher voucher2 = Voucher.builder().code(new RandomNumberGenerator().randomToString("VC", 5))
                .name("Sale sốc").value(new BigDecimal(100000))
                .startDate(new ConvertDateToLong().dateToLong("15/06/2023")).endDate(new ConvertDateToLong().dateToLong("25/06/2023"))
                .quantity(100).status(Status.DANG_SU_DUNG).build();
        voucherRepository.save(voucher2);
        voucherRepository.save(voucher1);

        Promotion promotion1 = Promotion.builder().code(new RandomNumberGenerator().randomToString("PR"))
                .name("diem").value(new BigDecimal(10))
                .startDate(new ConvertDateToLong().dateToLong("25/04/2023")).endDate(new ConvertDateToLong().dateToLong("01/06/2023"))
                .status(Status.DANG_SU_DUNG).build();
        Promotion promotion2 = Promotion.builder().code(new RandomNumberGenerator().randomToString("PR"))
                .name("diem2003").value(new BigDecimal(100))
                .startDate(new ConvertDateToLong().dateToLong("15/05/2023")).endDate(new ConvertDateToLong().dateToLong("01/07/2023"))
                .status(Status.KHONG_SU_DUNG).build();
        promotionRepository.save(promotion1);
        promotionRepository.save(promotion2);

        PromotionProductDetail promotionProductDetail1 = PromotionProductDetail.builder().promotion(promotion1)
                .productDetail(productDetail1).status(Status.DANG_SU_DUNG).build();
        PromotionProductDetail promotionProductDetail2 = PromotionProductDetail.builder().promotion(promotion2)
                .productDetail(productDetail2).status(Status.DANG_SU_DUNG).build();
        PromotionProductDetail promotionProductDetail3 = PromotionProductDetail.builder().promotion(promotion1)
                .productDetail(productDetail1).status(Status.DANG_SU_DUNG).build();
        PromotionProductDetail promotionProductDetail4 = PromotionProductDetail.builder().promotion(promotion2)
                .productDetail(productDetail1).status(Status.DANG_SU_DUNG).build();

        promotionProductDetailRepository.save(promotionProductDetail1);
        promotionProductDetailRepository.save(promotionProductDetail2);
        promotionProductDetailRepository.save(promotionProductDetail3);
        promotionProductDetailRepository.save(promotionProductDetail4);


        AccountVoucher accountVoucher1 = AccountVoucher.builder().account(account3).voucher(voucher1).status(Status.DANG_SU_DUNG).build();
        accountVoucherRepository.save(accountVoucher1);

        Address address1 = Address.builder().user(user2).status(Status.DANG_SU_DUNG).line("số 20 ngõ 19, Hoàng Hoa Thám").province("Hưng Yên").district("Huyện Văn Lâm")
                .ward("Xã Lạc Hồng").provinceId(268).toDistrictId(2046).wardCode("220906").build();
        addressRepository.save(address1);
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(DBGenerator.class);
        ctx.close();
    }
}
