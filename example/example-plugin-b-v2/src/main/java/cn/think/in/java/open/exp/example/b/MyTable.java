package cn.think.in.java.open.exp.example.b;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @version 1.0
 * @Author cxs
 * @Description
 * @date 2023/8/25
 **/
@Data
@TableName(value = "my_table")
public class MyTable {

    Integer id;

    String name;

    @Override
    public String toString() {
        return "MyTable{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}' + getClass().hashCode();
    }
}
