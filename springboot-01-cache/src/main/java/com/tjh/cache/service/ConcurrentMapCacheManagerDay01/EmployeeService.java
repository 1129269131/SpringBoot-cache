package com.tjh.cache.service.ConcurrentMapCacheManagerDay01;

import com.tjh.cache.bean.Employee;
import com.tjh.cache.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

@CacheConfig(cacheNames = "emp"/*,cacheManager = "employeeCacheManager"*/) //抽取缓存的公共配置
@Service
public class EmployeeService {

    @Autowired
    EmployeeMapper employeeMapper;

    /**
     * 将方法的运行结果进行缓存，以后再要相同的数据，直接从缓存中获取，不用调用方法
     *
     * CacheManager 管理多个Cache组件的，对缓存真正的CRUD操作在Cache组件中，每一个缓存组件有自己唯一一个名字
     * 几个属性：
     *      cacheNames/value：指定缓存组件的名字
     *          例：cacheNames = "emp" 或 cacheNames = {"emp","test"}
     *      key：缓存数据使用的key-》可以用它来指定。默认的key-value是：方法参数的值-方法的返回值（key-value）
     *          编写SpEL：#id：参数id的值（使用#a0、#p0、#root.args[0]是一样的，可参考尚硅谷文档）
     *      keyGenerator：key的生成器；可以自己指定key的生成器组件id（注意：key与keyGenerator只能二选一）
     *      cacheManager：指定缓存管理器
     *          ConcurrentMapCacheManager/RedisCacheManager
     *      cacheResolver：指定获取解析器
     *      condition：指定符合条件的情况下才缓存
     *          例：condition = "#id>0"
     *      unless：否定缓存-》当unless指定条件为true，方法的返回值不会被缓存
     *          例：unless = "#result == null"
     *      sync：是否使用异步模式
     * 测试：http://localhost:8080/emp/1
     * @param id
     * @return
     */
    @Cacheable(value = {"emp"})
    //@Cacheable(value = {"emp"},key = "#root.methodName+'['+#id+']'")
    //@Cacheable(value = {"emp"},keyGenerator = "myKeyGenerator",condition = "#id>1 and #root.methodName eq 'getEmp'",unless = "#id==2")
    public Employee getEmp(Integer id){
        System.out.println("查询"+id+"号员工");
        Employee emp = employeeMapper.getEmpById(id);
        return emp;
    }

    /**
     * @CachePut：既调用方法，又更新缓存数据
     * 修改数据库的某个数据，同时更新缓存
     * 运行时机：
     *  1、先调用更新方法
     *  2、后将更新方法的结果缓存起来
     * key = "#result.id"：使用返回后的id === key = "employee.id"：使用参数的员工id
     * 测试：http://localhost:8080/emp?id=1&lastName=张三&gender=0
     * @param employee
     * @return
     */
    @CachePut(value = "emp",key = "#result.id")
    public Employee updateEmp(Employee employee) {
        System.out.println("updateEmp：" + employee);
        employeeMapper.updateEmp(employee);
        return employee;
    }

    /**
     *  @CacheEvict：缓存清除
     *  key：指定要清除的数据
     *  allEntries：是否清除该缓存中的所有数据
     *  beforeInvocation：缓存的清除是否在方法之前执行（测试方法：解开int i = 10/0测试）
     *  测试：http://localhost:8080/delemp?id=1
     * @param id
     */
    //@CacheEvict(value = "emp",key = "#id")
    //@CacheEvict(value = "emp",allEntries = true)
    @CacheEvict(value = "emp",beforeInvocation = true)
    public void deleteEmp(Integer id){
        System.out.println("deleteEmp："+id);
        int i = 10/0;
    }

    /**
     * 测试：http://localhost:8080/emp/lastname/张三
     * @param lastName
     * @return
     */
    @Caching(
            cacheable = {
                    @Cacheable(value = "emp",key = "#lastName")
            },
            put = {
                    @CachePut(value = "emp",key = "#result.id"),
                    @CachePut(value = "emp",key = "#result.email")
            }
    )
    public Employee getEmpByLastName(String lastName){
        return employeeMapper.getEmpByLastName(lastName);
    }

}
