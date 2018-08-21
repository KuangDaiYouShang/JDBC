package Test;

import Handler.*;
import entity.Account;
import java.util.Date;

public class SaveAccount {
	public static void main(String args[]) {
		TemplateHandler temp = new MySQLTemplate();
		Account a = new Account();
		a.setID(1000);
		a.setMoney(2000);
		a.setName("zhangsan");
		a.setTime(new Date());
		temp.save(a);
		a.setID(1001);
		a.setMoney(2000);
		a.setName("lisi");
		a.setTime(new Date());
		temp.save(a);
	}
}
