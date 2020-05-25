package com.yhs.onlineshopping.controller;

import com.alibaba.fastjson.JSONObject;
import com.yhs.onlineshopping.dao.*;
import com.yhs.onlineshopping.file.FileController;
import com.yhs.onlineshopping.pojo.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("user")
public class UserController {
    @Autowired
    IResumeDao iResumeDao;
    @Autowired
    IProductsDao iProductsDao;
    @Autowired
    ICategoryDao iCategoryDao;
    @Autowired
    IUserDao iUserDao;
    @Autowired
    IShoppingCarDao iShoppingCarDao;
    @Autowired
    IOrderDao iOrderDao;

    @ApiOperation("登录View")
    @RequestMapping("/toLogin")
    public ModelAndView toLogin(ModelAndView modelAndView){
        modelAndView.setViewName("user/login");
        return modelAndView;
    }
    @ApiOperation("退出")
    @RequestMapping("/toSingOut")
    public ModelAndView toSingOut(ModelAndView modelAndView){
        modelAndView.setViewName("user/login");
        return modelAndView;
    }
    @ApiOperation("发布页面")
    @RequestMapping("/toPublish")
    public ModelAndView toPublish(ModelAndView modelAndView){
        modelAndView.setViewName("user/publish");
        return modelAndView;
    }
    @ApiOperation("浏览我的账户信息")
    @RequestMapping("/myAccount")
    public ModelAndView myAccount(ModelAndView modelAndView,HttpServletRequest request){
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("loginName");
        User user = iUserDao.toFindUidByName(username);
        List<Resume> resumes = iResumeDao.toFindResumesByUid(user.getUid());
        modelAndView.addObject("resumes",resumes);
        modelAndView = toFindOrders(modelAndView,request);
        modelAndView.setViewName("user/myAccount");
        return modelAndView;
    }

    @ApiOperation("模糊查询")
    @ResponseBody
    @RequestMapping("/toFindProductsByKeyWords")
    public ModelAndView toFindProductsByKeyWords(ModelAndView modelAndView,HttpServletRequest request){
        String name = request.getParameter("name");
        int cateid = Integer.parseInt(request.getParameter("cateId"));
        System.out.println(name);
        List<Products> products = iProductsDao.toFindManyProductsByKeyWords(name,cateid);
        modelAndView.addObject("products",products);
        System.out.println(products.toString());
        modelAndView.setViewName("user/product");
        return modelAndView;
    }
    @RequestMapping("/fileDownload")
    public void flieDownload(String filepath, HttpServletResponse response ){
        try {
            FileController fileController = new FileController();
            fileController.downloader(filepath,response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @ApiOperation("用户登陆验证")
    @ResponseBody
    @RequestMapping("/doLogin")
    public ModelAndView doLogin(ModelAndView modelAndView, @RequestParam("username") String username,
                                @RequestParam("password") String password, HttpServletRequest request){
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        System.out.println(username+password);
        Integer result = iUserDao.doLogin(user);
        System.out.println("result:"+result);
        if (result!=null) {
            HttpSession session = request.getSession();
            session.setAttribute("loginName", username);
            modelAndView.setViewName("user/index");
        }
        else {
            System.out.println("账号密码错误！");
            modelAndView.setViewName("user/login");
        }
        return modelAndView;
    }
    @ApiOperation("注册View")
    @RequestMapping("/register")
    public ModelAndView register(ModelAndView modelAndView){
        modelAndView.setViewName("user/register");
        return modelAndView;
    }
    @ApiOperation("返回用户首页")
    @RequestMapping("/reIndex")
    public ModelAndView reIndex(ModelAndView modelAndView){
        modelAndView.setViewName("user/index");
        return modelAndView;
    }
    @ApiOperation("用户注册功能实现")
    @ResponseBody
    @RequestMapping("/registerSucess")
    public ModelAndView registerSucess(ModelAndView modelAndView,HttpServletRequest request){
        String password = request.getParameter("password");
        String rePassword = request.getParameter("repassword");
        if (password.equals(rePassword)){
        User user = new User();
        user.setUsername(request.getParameter("username"));
        user.setPassword(password);
        user.setPhone(request.getParameter("phone"));
        user.setAddress(request.getParameter("address"));
        user.setEmail(request.getParameter("email"));
        iUserDao.registerUser(user);
        modelAndView.setViewName("/user/index");
        }else {
            modelAndView.setViewName("/user/register");
        }
        return modelAndView;
    }

    @ApiOperation("显示跳蚤市场")
    @ResponseBody
    @RequestMapping("/toDisplayProductsByCateid")
    public ModelAndView toDisplayProductsByCateid(ModelAndView modelAndView){
        List<Products> products = iProductsDao.toDisplayProductsByCateid(1);
        modelAndView.addObject("products",products);
        modelAndView.setViewName("user/product");
        return modelAndView;
    }
    @ApiOperation("显示商品种类")
    @ResponseBody
    @RequestMapping("/toDisplayCates")
    public ModelAndView toDisplayCates(ModelAndView modelAndView){
        List<Category> categories = iCategoryDao.findAllCategories();
        List<Category> limitCategories = iCategoryDao.toFindCategoryLimitSix();
        List<Products> limitProducts = iProductsDao.toFindSevenProduct();
        modelAndView.addObject("categories",categories);
        modelAndView.addObject("limitCategories",limitCategories);
        modelAndView.addObject("limitProducts",limitProducts);
        modelAndView.setViewName("user/categoryInfo");
        return modelAndView;
    }

    @ApiOperation("根据商品cateid查询商品")
    @ResponseBody
    @RequestMapping("/toFindProductsBycateId")
    public ModelAndView toFindProductsBycateId(ModelAndView modelAndView,int cateid){
        List<Products> products = iCategoryDao.toFindProductsBycateId(cateid);
        modelAndView.addObject("products",products);
        modelAndView.setViewName("user/cateProduct");
        return modelAndView;
    }
    @ApiOperation("查询汽车服务")
    @ResponseBody
    @RequestMapping("/toDisplayCarService")
    public ModelAndView toDisplayCarService(ModelAndView modelAndView){
        List<Products> products = iCategoryDao.toFindProductsBycateId(3);
        modelAndView.addObject("products",products);
        modelAndView.setViewName("user/cateProduct");
        return modelAndView;
    }
    @ApiOperation("导航栏根据商品cateid查询商品")
    @ResponseBody
    @RequestMapping("/FindProductsBycateId")
    public ModelAndView FindProductsBycateId(ModelAndView modelAndView){
        List<Products> products = iCategoryDao.toFindProductsBycateId(2);
        modelAndView.addObject("products",products);
        modelAndView.setViewName("user/cateProduct");
        return modelAndView;
    }
    @ApiOperation("发布商品")
    @ResponseBody
    @RequestMapping("/publishProduct")
    public ModelAndView  publishProduct(ModelAndView modelAndView,HttpServletRequest request,
                                            @RequestParam("file") MultipartFile file,@RequestParam("name") String name) throws Exception {
        HttpSession session = request.getSession();
        String loginName = (String)session.getAttribute("loginName");
        if (loginName == null){
            modelAndView.setViewName("user/login");
            return modelAndView;
        }
        FileController fileController = new FileController();
        List<String> fileInfo = fileController.upLoader(file,request);
        String flieName = fileInfo.get(1);
        Products products = new Products();
        products.setName(name);
        products.setCateid(Integer.parseInt(request.getParameter("cateid")));
        products.setImage(flieName);
        products.setPrice(Float.parseFloat(request.getParameter("price")));
        iCategoryDao.toAddProductByCateId(products);
        List<Products> productsList = iProductsDao.toDisplayProductsByCateid(products.getCateid());
        modelAndView.addObject("products",productsList);
        modelAndView.setViewName("user/product");
        return modelAndView;
    }

    @ApiOperation("商品细节")
    @ResponseBody
    @RequestMapping("/productDetail")
    public ModelAndView productdetil(ModelAndView modelAndView,HttpServletRequest request,int pid){
        List<Products> products = iProductsDao.toFindFourProduct();
        Products product = iProductsDao.toFindProductByPid(pid);
        modelAndView.addObject("product",product);
        modelAndView.addObject("productsFour",products);
       if(product.getCateid()!=2){
           modelAndView.setViewName("user/productDetail");
       }else{
            modelAndView.setViewName("user/workDetail");
        }
        return modelAndView;
    }
    @ApiOperation("添加购物车")
    @ResponseBody
    @RequestMapping("/toAddShoppingCar")
    public ModelAndView toAddShoppingCar(ModelAndView modelAndView,HttpServletRequest request,int pid){
            HttpSession session = request.getSession();
            String username = (String) session.getAttribute("loginName");
            System.out.println(username);
            User user = iUserDao.toFindUidByName(username);
            Products product = iProductsDao.toFindProductByPid(pid);
            Integer res = iShoppingCarDao.toFindShoppingByName(product.getName());
            if(res == null){
                ShoppingCar shoppingCar = new ShoppingCar();
                shoppingCar.setUid(user.getUid());
                shoppingCar.setName(product.getName());
                shoppingCar.setImage(product.getImage());
                shoppingCar.setCount(1);
                shoppingCar.setPrice(product.getPrice());
                shoppingCar.setPhone(user.getPhone());
                shoppingCar.setAddress(user.getAddress());
                iShoppingCarDao.toAddShoppingCar(shoppingCar);
            }else {
                ShoppingCar shoppingCar = iShoppingCarDao.toFindShoppingByCid(res);
                shoppingCar.setCount(shoppingCar.getCount()+1);
                iShoppingCarDao.toAlterCount(shoppingCar);
            }
            List<Products> products = iProductsDao.toFindFourProduct();
            modelAndView.addObject("productsFour",products);
            modelAndView.addObject("product",product);
            modelAndView.setViewName("user/productDetail");
            return modelAndView;
    }
    @ApiOperation("从商品细节页面添加购物车")
    @ResponseBody
    @RequestMapping("/toAddShoppingCarFromDetail")
    public ModelAndView toAddShoppingCarFromDetail(ModelAndView modelAndView,HttpServletRequest request,int pid){
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("loginName");
        User user = iUserDao.toFindUidByName(username);
        Products product = iProductsDao.toFindProductByPid(pid);
        Integer res = iShoppingCarDao.toFindShoppingByName(product.getName());
        if(res == null){
            Float price = Integer.parseInt(request.getParameter("salesCount"))*product.getPrice();
            ShoppingCar shoppingCar = new ShoppingCar();
            shoppingCar.setUid(user.getUid());
            shoppingCar.setName(product.getName());
            shoppingCar.setImage(product.getImage());
            shoppingCar.setCount(1);
            shoppingCar.setPrice(price);
            shoppingCar.setPhone(user.getPhone());
            shoppingCar.setAddress(user.getAddress());
            iShoppingCarDao.toAddShoppingCar(shoppingCar);
        }else {
            ShoppingCar shoppingCar = iShoppingCarDao.toFindShoppingByCid(res);
            shoppingCar.setCount(shoppingCar.getCount()+1);
            iShoppingCarDao.toAlterCount(shoppingCar);
        }
        List<Products> products = iProductsDao.toFindFourProduct();
        modelAndView.addObject("productsFour",products);
        modelAndView.addObject("product",product);
        modelAndView.setViewName("user/productDetail");
        return modelAndView;
    }
    @ApiOperation("投递简历页面")
    @ResponseBody
    @RequestMapping("/toVoteView")
    public ModelAndView toVoteView(ModelAndView modelAndView,int pid){
        Products product = iProductsDao.toFindProductByPid(pid);
        modelAndView.addObject("product",product);
        modelAndView.setViewName("user/vote");
        return modelAndView;
    }
    @ApiOperation("投递简历")
    @ResponseBody
    @RequestMapping("/toVote")
    public ModelAndView toVote(ModelAndView modelAndView, HttpServletRequest request, int pid, @RequestParam("file")MultipartFile file) throws Exception {
        Products product = iProductsDao.toFindProductByPid(pid);
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("loginName");
        User user = iUserDao.toFindUidByName(username);
        FileController fileController = new FileController();
        List<String> fileInfo = fileController.upLoader(file,request);
        String flieName = fileInfo.get(1);
        Resume resume = new Resume();
        resume.setUsername(user.getUsername());
        resume.setAddress(request.getParameter("address"));
        resume.setCateid(product.getCateid());
        resume.setImage(product.getImage());
        resume.setPhone(request.getParameter("phone"));
        resume.setPid(pid);
        resume.setPname(product.getName());
        resume.setPrice(product.getPrice());
        resume.setResume(flieName);
        resume.setUid(user.getUid());
        iResumeDao.toWorkVote(resume);
        modelAndView  = myAccount(modelAndView,request);
        return modelAndView;
    }
    @ApiOperation("查看购物车")
    @ResponseBody
    @RequestMapping("/toDisplayShoppingCar")
    public ModelAndView toDisplayShoppingCar(ModelAndView modelAndView,HttpServletRequest request){
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("loginName");
        User user = iUserDao.toFindUidByName(username);
        List<ShoppingCar> shoppingCars = iShoppingCarDao.toFindShoppingCarByUid(user.getUid());
        modelAndView.addObject("shoppingCars",shoppingCars);
        modelAndView.setViewName("user/shoppingCar");
        return modelAndView;
    }
    @ApiOperation("删除购物车")
    @ResponseBody
    @RequestMapping("/toDeleteShoppingCarByCid")
    public ModelAndView toDeleteShoppingCarByCid(ModelAndView modelAndView,HttpServletRequest request,int cid) throws Exception {
        iShoppingCarDao.toDeleteShoppingCarByCid(cid);
        modelAndView =toDisplayShoppingCar(modelAndView,request);
        return modelAndView;
    }
    @ApiOperation("购买商品")
    @ResponseBody
    @RequestMapping("/toBuyProduct")
    public ModelAndView toBuyProduct(ModelAndView modelAndView,HttpServletRequest request) throws Exception {
        int cid = Integer.parseInt(request.getParameter("cid"));
        System.out.println(cid);
        ShoppingCar shoppingCar = iShoppingCarDao.toFindShoppingByCid(cid);
        System.out.println(shoppingCar.toString());
        Order order = new Order();
        order.setUid(shoppingCar.getUid());
        order.setName(shoppingCar.getName());
        order.setImage(shoppingCar.getImage());
        order.setCount(Integer.parseInt(request.getParameter("count")));
        order.setPrice(shoppingCar.getPrice()*Integer.parseInt(request.getParameter("count")));
        order.setAddress(shoppingCar.getAddress());
        order.setPhone(shoppingCar.getPhone());
        iShoppingCarDao.toBuyProduct(order);
        modelAndView =toDisplayShoppingCar(modelAndView,request);
        return modelAndView;
    }
    @ApiOperation("查询订单")
    @ResponseBody
    @RequestMapping("/toFindOrders")
    public ModelAndView toFindOrders(ModelAndView modelAndView,HttpServletRequest request){
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("loginName");
        User user = iUserDao.toFindUidByName(username);
        List<Order> orders = iOrderDao.toFindOrdersByUid(user.getUid());
        modelAndView.addObject("user",user);
        modelAndView.addObject("orders",orders);
        return modelAndView;
    }
    @ApiOperation("获得用户基本信息")
    @ResponseBody
    @RequestMapping("/toDisplayUsers")
    public JSONObject toDisplayUsers(int uid){
        JSONObject jsonObject = new JSONObject();
        String msg = "";
        String code = "";
        User user = iUserDao.toFindUserById(uid);
        if (user == null)
        {
            code = "500";
            msg = "user为空";
        }else {
            code = "200";
            msg = "ok";
        }
        jsonObject.put("data",user);
        jsonObject.put("code",code);
        jsonObject.put("msg",msg);
        return jsonObject;
    }
    @ApiOperation("修改用户地址")
    @ResponseBody
    @RequestMapping("/toAlterAddress")
    public ModelAndView toAlterAddress(int uid,HttpServletRequest request,ModelAndView modelAndView){
        User user = new User();
        try {
            user.setUid(uid);
            user.setAddress(request.getParameter("address"));
            iUserDao.toAlterAddressById(user);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        modelAndView = myAccount(modelAndView,request);
        return modelAndView;
    }

    @ApiOperation("修改用户密码")
    @ResponseBody
    @RequestMapping("/toAlterUserPassword")
    public ModelAndView toAlterUserPassword(int uid,HttpServletRequest request,ModelAndView modelAndView){
        User user = new User();
        try {
            user.setUid(uid);
            user.setPassword(request.getParameter("newPassword"));
            iUserDao.toAlterPasswordById(user);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        modelAndView = myAccount(modelAndView,request);
        return modelAndView;
    }

}
