package dominic.mybatis.bean;


import lombok.Data;

import java.io.Serializable;
import java.util.List;
@Data
public class PageList<T> extends PageParam implements Serializable{
	private long count = 0;
	private List<T> list;

	public static <T> PageList<T> build(List<T> list, long count, PageParam param) {
		PageList<T> pageList = new PageList<>();
		pageList.setList(list);
		pageList.setCount(count);
		if (null != param) {
			pageList.setPageIndex(param.getPageIndex());
			pageList.setPageSize(param.getPageSize());
		}
		return pageList;
	}
}
