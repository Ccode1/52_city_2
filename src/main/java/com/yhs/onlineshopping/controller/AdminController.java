package com.yhs.onlineshopping.controller;


import com.yhs.onlineshopping.dao.*;
import com.yhs.onlineshopping.file.FileController;
import com.yhs.onlineshopping.pojo.*;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("admin")
public class AdminController {
    @Autowired
    ILoginDao iLoginDao;
    @Autowired
    IProductsDao iProductsDao;
    @Autowired
    ICategoryDao iCategoryDao;
    @Autowired
    IOrderDao iOrderDao;
    /**注入redisTemplate**/
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    IResumeDao iResumeDao;
    //@ResponseBody
    @RequestMapping("/toLogin")
    public ModelAndView toLogin(ModelAndView modelAndView){
        modelAndView.setViewName("admin/login");
        return modelAndView;
    }
    @ApiOperation("管理员登陆验证")
    @ResponseBody
    @RequestMapping("/doLogin")
    public ModelAndView doLogin(ModelAndView modelAndView, @RequestParam("username") String username,
                                @RequestParam("password") String password, HttpServletRequest request){
        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setPassword(password);
        System.out.println(username+password);
        Integer result = iLoginDao.doLogin(admin);
        System.out.println("result:"+result);
        if (result!=null) {
            HttpSession session = request.getSession();
            session.setAttribute("loginName", username);
            modelAndView.setViewName("admin/index");
        }
        else {
            System.out.println("账号密码错误！");
            modelAndView.setViewName("admin/login");
        }
        return modelAndView;
    }
    @ApiOperation("显示所有商品测试类")
    @ResponseBody
    @RequestMapping("/toDisplayProducts")
    public ModelAndView toDisplayProducts(ModelAndView modelAndView,HttpServletRequest request){
        HttpSession session = request.getSession();
        String loginName = (String)session.getAttribute("loginName");
        if (loginName == null){
            modelAndView.setViewName("admin/login");
            return modelAndView;
        }
        List<Products> products = iProductsDao.toDisplayProducts();
        System.out.println(products.toString());
        modelAndView.addObject("products",products);
        modelAndView.setViewName("admin/products");
        return modelAndView;
    }
    @ApiOperation("根据商品ID删除商品")
    @ResponseBody
    @RequestMapping("/deleteProductById")
    public ModelAndView deleteProductById(ModelAndView modelAndView,HttpServletRequest request,int pid){
        HttpSession session = request.getSession();
        String loginName = (String)session.getAttribute("loginName");
        if (loginName==null){
            modelAndView.setViewName("admin/login");
            return modelAndView;
        }
        iProductsDao.deleteProductById(pid);
        toDisplayProducts(modelAndView,request);
        return modelAndView;
    }
    @ResponseBody
    @RequestMapping("/toShowProductAlterPage")
    public ModelAndView toShowProductAlterPage(ModelAndView modelAndView ,int pid){
        Products product = iProductsDao.toFindProductByPid(pid);
        modelAndView.addObject("product",product);
        modelAndView.setViewName("admin/alterProduct");
        return modelAndView;
    }
    @ApiOperation("根据Pid修改商品信息,修改成功后跳转至商品查询页面")
    @ResponseBody
    @RequestMapping("/toAlterPrdouctSuccess")
    public ModelAndView toAlterPrdouctSuccess(ModelAndView modelAndView,HttpServletRequest request,int pid){
        HttpSession session = request.getSession();
        String loginName = (String)session.getAttribute("loginName");
        if (loginName == null){
            modelAndView.setViewName("admin/login");
            return modelAndView;
        }
        Products products =new Products();
        products.setPid(pid);
        products.setName(request.getParameter("name"));
        products.setPrice(Float.parseFloat(request.getParameter("price")));
        iProductsDao.toAlterPrdouctSuccess(products);
        toDisplayProducts(modelAndView,request);
        return modelAndView;
    }
    @ApiOperation("显示商品种类")
    @ResponseBody
    @RequestMapping("/toDisplayCates")
    public ModelAndView toDisplayCates(ModelAndView modelAndView,HttpServletRequest request){
        HttpSession session = request.getSession();
        String loginName = (String)session.getAttribute("loginName");
        if (loginName == null){
            modelAndView.setViewName("admin/login");
            return modelAndView;
        }
        List<Category> categories = iCategoryDao.findAllCategories();
        modelAndView.addObject("categories", categories);
        System.out.println(categories.toString());
        modelAndView.setViewName("admin/category");
        return modelAndView;
    }
    @ApiOperation("添加商品种类")
    @ResponseBody
    @RequestMapping("/toAddCategories")
    public ModelAndView toAddCategories(ModelAndView modelAndView,HttpServletRequest request,@RequestParam("file") MultipartFile file) throws Exception {
        HttpSession session = request.getSession();
        String loginName = (String)session.getAttribute("loginName");
        if (loginName == null){
            modelAndView.setViewName("admin/login");
            return modelAndView;
        }
        FileController fileController = new FileController();
        List<String> fileInfo = fileController.upLoader(file,request);
        String flieName = fileInfo.get(1);
        Category category = new Category();
        category.setCatename(request.getParameter("cateName"));
        category.setImage(flieName);
        iCategoryDao.toAddCate(category);
        toDisplayCates(modelAndView, request);
        return modelAndView;
    }
    @ApiOperation("从商品种类添加单词页面跳转")
    @ResponseBody
    @RequestMapping("/toAddProduct")
    public ModelAndView toAddProduct(ModelAndView modelAndView,HttpServletRequest request,int cateid){
        HttpSession session = request.getSession();
        String loginName = (String)session.getAttribute("loginName");
        if (loginName == null){
            modelAndView.setViewName("admin/login");
            return modelAndView;
        }
        modelAndView.addObject("cateid",cateid);
        modelAndView.setViewName("admin/addProduct");
        return modelAndView;
    }
    @ApiOperation("从商品种类添加商品功能实现")
    @ResponseBody
    @RequestMapping("/toAddProductFromCateId")
    public ModelAndView  toAddWordsFromCate(ModelAndView modelAndView,HttpServletRequest request,int cateid,
                                            @RequestParam("file") MultipartFile file,@RequestParam("name") String name) throws Exception {
        HttpSession session = request.getSession();
        String loginName = (String)session.getAttribute("loginName");
        if (loginName == null){
            modelAndView.setViewName("admin/login");
            return modelAndView;
        }
        FileController fileController = new FileController();
        List<String> fileInfo = fileController.upLoader(file,request);
        String flieName = fileInfo.get(1);
        Products products = new Products();
        products.setName(name);
        products.setCateid(cateid);
        products.setImage(flieName);
        products.setPrice(Float.parseFloat(request.getParameter("price")));
        iCategoryDao.toAddProductByCateId(products);
        toDisplayProducts(modelAndView,request);
        return modelAndView;
    }
    @ApiOperation("删除商品种类")
    @ResponseBody
    @RequestMapping("/deleteCategory")
    public ModelAndView toDeleteCategory(ModelAndView modelAndView,HttpServletRequest request,int cateid){
        Category category = new Category();
        System.out.println(cateid);
        category.setCateid(cateid);
        iCategoryDao.toDeleteCate(category);
        toDisplayCates(modelAndView, request);
        return modelAndView;
    }
    @ApiOperation("展示所有商品订单")
    @ResponseBody
    @RequestMapping("/toDisplayOrders")
    public ModelAndView toDisplayOrders(ModelAndView modelAndView,HttpServletRequest request){
        HttpSession session = request.getSession();
        String loginName = (String)session.getAttribute("loginName");
        if (loginName==null){
            modelAndView.setViewName("admin/login");
            return modelAndView;
        }
        List<Order> orders = iOrderDao.toFindAllOrders();
        modelAndView.addObject("orders",orders);
        modelAndView.setViewName("admin/orders");
        return modelAndView;
    }
    @ApiOperation("根据oid删除商品订单")
    @ResponseBody
    @RequestMapping("/deleteOrderById")
    public ModelAndView deleteOrderById(ModelAndView modelAndView,HttpServletRequest request,int oid){
        HttpSession session = request.getSession();
        String loginName = (String)session.getAttribute("loginName");
        if (loginName==null){
            modelAndView.setViewName("admin/login");
            return modelAndView;
        }
        try{
            iOrderDao.toDeleteOrderById(oid);
        }catch (Exception e) {
            e.printStackTrace();
        }
        toDisplayOrders(modelAndView,request);
        return modelAndView;
    }
    @ApiOperation("修改订单页面跳转")
    @ResponseBody
    @RequestMapping("/toShowOrderAlterPage")
    public ModelAndView toShowOrderAlterPage(ModelAndView modelAndView ,int oid){
        Order order = iOrderDao.toFindOrderById(oid);
        modelAndView.addObject("order",order);
        modelAndView.setViewName("admin/alterOrder");
        return modelAndView;
    }
    @ApiOperation("根据oid修改商品信息,修改成功后跳转至订单查询页面")
    @ResponseBody
    @RequestMapping("/toAlterOrderSuccess")
    public ModelAndView toAlterOrderSuccess(ModelAndView modelAndView,HttpServletRequest request,int oid){
        HttpSession session = request.getSession();
        String loginName = (String)session.getAttribute("loginName");
        if (loginName == null){
            modelAndView.setViewName("admin/login");
            return modelAndView;
        }
        Order order = new Order();
        order.setOid(oid);
        order.setName(request.getParameter("name"));
        order.setAddress(request.getParameter("address"));
        order.setPhone(request.getParameter("phone"));
        order.setCount(Integer.parseInt(request.getParameter("count")));
        Float price = Float.parseFloat(request.getParameter("price"));
        price = price*order.getCount();
        order.setPrice(price);
        System.out.println(order.getCount()+ ":"+order.getPrice());
        iOrderDao.toAlterOrderById(order);
        toDisplayOrders(modelAndView,request);
        return modelAndView;
    }
    @ApiOperation("修改订单页面跳转")
    @ResponseBody
    @RequestMapping("/toAddOrderPage")
    public ModelAndView toAddOrderPage(ModelAndView modelAndView ){
        modelAndView.setViewName("admin/addOrder");
        return modelAndView;
    }
    @ApiOperation("根据oid修改商品信息,修改成功后跳转至订单查询页面")
    @ResponseBody
    @RequestMapping("/toAddOrderSuccess")
    public ModelAndView toAddOrderSuccess(ModelAndView modelAndView,HttpServletRequest request,
                                          @RequestParam("file")MultipartFile file) throws Exception {
        HttpSession session = request.getSession();
        String loginName = (String)session.getAttribute("loginName");
        if (loginName == null){
            modelAndView.setViewName("admin/login");
            return modelAndView;
        }
        FileController fileController = new FileController();
        List<String> fileInfo = fileController.upLoader(file,request);
        String filePath = fileInfo.get(1);
        Order order = new Order();
        order.setName(request.getParameter("name"));
        order.setUid(Integer.parseInt(request.getParameter("uid")));
        order.setAddress(request.getParameter("address"));
        order.setPhone(request.getParameter("phone"));
        order.setImage(filePath);
        order.setCount(Integer.parseInt(request.getParameter("count")));
        Float price = Float.parseFloat(request.getParameter("price"));
        price = price*order.getCount();
        order.setPrice(price);
        iOrderDao.toAddOrder(order);
        toDisplayOrders(modelAndView,request);
        return modelAndView;
    }

    @ApiOperation("管理员查询投递简历情况")
    @ResponseBody
    @RequestMapping("/toDisplayResume")
    public ModelAndView toDisplayResume(ModelAndView modelAndView,HttpServletRequest request){
        HttpSession session = request.getSession();
        String loginName = (String)session.getAttribute("loginName");
        if (loginName==null){
            modelAndView.setViewName("admin/login");
            return modelAndView;
        }
        List<Resume> resumes = iResumeDao.toDisplayResumes();
        modelAndView.addObject("resumes",resumes);
        modelAndView.setViewName("admin/resumes");
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
}
