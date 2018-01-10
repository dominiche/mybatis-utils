package dominic.mybatis.support.stream;

import com.google.common.collect.Maps;
import dominic.mybatis.support.Restriction;
import dominic.mybatis.support.RestrictionUnit;
import dominic.mybatis.support.appender.RestrictionAppender;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by Administrator:herongxing on 2017/4/18 16:21.
 */
public class Restrictions extends RestrictionUnit {

    private RestrictionAppender appender;

    Restrictions(RestrictionAppender appender) {
        this.appender = appender;
        HashMap<String, Object> hashMap = Maps.newHashMap();
        appender.getAppenderList().forEach(restriction -> hashMap.putAll(restriction.getParamMap()));
        setParamMap(hashMap);
    }

    public static Restrictions.RestrictionsBuilder builder() {
        return new Restrictions.RestrictionsBuilder();
    }
    public static Restrictions.RestrictionsBuilder builder(Restrictions restrictions) {
        return new Restrictions.RestrictionsBuilder(restrictions.appender);
    }

    @Override
    public String SQL() {
        return appender.SQL();
    }

    @Override
    public String toString() {
        return SQL();
    }

    public static class RestrictionsBuilder {
        private RestrictionAppender appender;
        RestrictionsBuilder() {
            this.appender = RestrictionAppender.newInstance();
        }
        RestrictionsBuilder(RestrictionAppender appender) {
            this.appender = appender;
        }

        public Restrictions build() {
            return new Restrictions(appender);
        }
        public String SQL() {
            return new Restrictions(appender).SQL();
        }

        public <T> Restrictions.RestrictionsBuilder eq(String name, T value) {
            appender.append(Restriction.eq(name, value));
            return this;
        }
        public <T> Restrictions.RestrictionsBuilder notEq(String name, T value) {
            appender.append(Restriction.notEq(name, value));
            return this;
        }
        public <T> Restrictions.RestrictionsBuilder isNull(String name) {
            appender.append(Restriction.isNull(name));
            return this;
        }
        public <T> Restrictions.RestrictionsBuilder isNotNull(String name) {
            appender.append(Restriction.isNotNull(name));
            return this;
        }
        public <T> Restrictions.RestrictionsBuilder in(String name, Collection<T> collection) {
            appender.append(Restriction.in(name, collection));
            return this;
        }
        public Restrictions.RestrictionsBuilder like(String name, String value) {
            appender.append(Restriction.like(name, value));
            return this;
        }
        public Restrictions.RestrictionsBuilder likeBoth(String name, String value) {
            appender.append(Restriction.likeBoth(name, value));
            return this;
        }
        public Restrictions.RestrictionsBuilder greaterEqual(String name, Object value) {
            appender.append(Restriction.greaterEqual(name, value));
            return this;
        }
        public Restrictions.RestrictionsBuilder greaterThan(String name, Object value) {
            appender.append(Restriction.greaterThan(name, value));
            return this;
        }
        public Restrictions.RestrictionsBuilder lessEqual(String name, Object value) {
            appender.append(Restriction.lessEqual(name, value));
            return this;
        }
        public Restrictions.RestrictionsBuilder lessThan(String name, Object value) {
            appender.append(Restriction.lessThan(name, value));
            return this;
        }
//        public Restrictions.RestrictionsBuilder or(Restrictions restrictions) {
//            appender.append(Restriction.sql(restrictions.SQL()));
//            return this;
//        }//还不支持
        public Restrictions.RestrictionsBuilder sql(String restriction) {
            appender.append(Restriction.sql(restriction));
            return this;
        }


    }
}
