import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//Mức tồn tại được bộ dịch nhận ra, nhưng không được nhận biết bởi máy ảo tại Runtime
//@Retention(RetentionPolicy.CLASS)

//Tồn tại trên code nguồn, không được bộ dịch(compiler) nhận ra
//@Retention(RetentionPolicy.SOURCE)

//Mức tồn tại lớn nhat, được bộ dịch nhận biết và máy ảo jvm cũng nhận ra khi chạy chương trình
@Retention(RetentionPolicy.RUNTIME)

//chú thich phạm vi sử dụng
@Target(ElementType.TYPE)
public @interface Entity {
    String tableName();
}
