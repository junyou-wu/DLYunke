package com.wu.web;

import com.github.pagehelper.PageInfo;
import com.wu.Query.ClueRemarkQuery;
import com.wu.Query.CustomerQuery;
import com.wu.Query.CustomerRemarkQuery;
import com.wu.model.TClue;
import com.wu.model.TClueRemark;
import com.wu.model.TCustomer;
import com.wu.model.TCustomerRemark;
import com.wu.result.R;
import com.wu.service.CustomerService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
public class CustomController {
    @Resource
    CustomerService customerService;

    @PostMapping("/api/clue/customer")
    public R convertCustomer(@RequestBody CustomerQuery customerQuery, @RequestHeader(value = "Authorization") String token){
        customerQuery.setToken(token);
        Boolean convert = customerService.convertCustomer(customerQuery);
        return convert ? R.OK() : R.FAIL() ;
    }

    @GetMapping(value = "/api/customers")
    public R cluePage(@RequestParam(value = "current", required = false) Integer current) {
        if (current == null) {
            current = 1;
        }

        PageInfo<TCustomer> pageInfo = customerService.getCustomerByPage(current);
        return R.OK(pageInfo);
    }

    @GetMapping("/api/customer/detail/{id}")
    public R loadCustomer(@PathVariable(value = "id") Integer id){
        TCustomer tCustomer = customerService.getCustomerById(id);
        return R.OK(tCustomer);
    }

    @PostMapping("/api/customer/remark")
    public R addCustomerRemark(@RequestBody CustomerRemarkQuery customerRemarkQuery, @RequestHeader(value = "Authorization") String token) {
        //axios提交post请求，提交过来的是json数据，使用@RequestBody注解接收
        customerRemarkQuery.setToken(token);
        int save = customerService.saveCustomerRemark(customerRemarkQuery);
        return save >= 1 ? R.OK( ) : R.FAIL();
    }

    @GetMapping("/api/customers/remark")
    public R clueRemarkPage(@RequestParam(value = "current", required = false) Integer current,
                            @RequestParam(value = "customerId") Integer customerId) {

        CustomerRemarkQuery customerRemarkQuery = new CustomerRemarkQuery();
        customerRemarkQuery.setCustomerId(customerId);

        if (current == null) {
            current = 1;
        }
        PageInfo<TCustomerRemark> pageInfo = customerService.getCustomerRemarkByPage(current, customerRemarkQuery);
        return R.OK(pageInfo);
    }

    @GetMapping("/api/customer/remark/{id}")
    public R getRemark(@PathVariable(value = "id") Integer id){

        TCustomerRemark tCustomerRemark =  customerService.getRemark(id);

        return R.OK(tCustomerRemark);
    }

    @PostMapping("/api/customer/remark/edit")
    public R editCustomerRemark(@RequestBody CustomerRemarkQuery customerRemarkQuery, @RequestHeader(value = "Authorization") String token) {
        customerRemarkQuery.setToken(token);
        int update = customerService.updateCustomerRemark(customerRemarkQuery);
        return update >= 1 ? R.OK( ) : R.FAIL();
    }

    @GetMapping("/api/customer/remark/delete/{id}")
    public R delRemark(@PathVariable(value = "id") Integer id){

        int delete =  customerService.deleteRemark(id);

        return delete >= 1 ? R.OK() : R.FAIL();
    }
}
