package dominic.mybatis.constants;

public enum StringTypePolicy {
	/**
     * 相等
	 */
	EQUAL,
	/**
	 * 左like，即：like ‘...%’
	 */
	LIKE,
	/**
	 * 左右都有like，即：like ‘%...%’
	 */
	LIKE_BOTH
}