package com.planonsoftware.tms.constants;

public class Constants {
		public static final String CHECK_LOGIN="select count(*) from login where username=? and password=?";
		public static final String GET_LOGIN_DETAILS = "SELECT a.fullname, a.address, a.joining_date, a.no_of_order_till_now, a.email, a.username,b.ph_no"
	             +" FROM public.user_info a"
                 +" inner join public.login b"
                +" on a.username=b.username where b.username=?";
}
