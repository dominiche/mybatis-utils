package dominic.mybatis.support.stream;

import com.google.common.collect.Maps;
import dominic.mybatis.support.Condition;
import dominic.mybatis.support.Restriction;
import dominic.mybatis.support.RestrictionUnit;
import dominic.mybatis.support.appender.AbstractAppender;
import dominic.mybatis.support.appender.ConditionAppender;
import org.apache.commons.collections.MapUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator:herongxing on 2017/4/18 16:21.
 */
public class Restrictions extends RestrictionUnit {

    private ConditionAppender appender;

    Restrictions(ConditionAppender appender) {
        this.appender = appender;
        HashMap<String, Object> hashMap = Maps.newHashMap();
        appender.getAppenderList().forEach(condition -> {
            Map<String, Object> paramMap = condition.getRestriction().getParamMap();
            if (MapUtils.isNotEmpty(paramMap)) {
                hashMap.putAll(paramMap);
            }
        });
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

    public Restrictions and(RestrictionUnit restriction) {
        return add(restriction, AbstractAppender.AND);
    }

    public Restrictions or(RestrictionUnit restriction) {
        return add(restriction, AbstractAppender.OR);
    }

    private Restrictions add(RestrictionUnit restriction, String separator) {
        appender.append(Condition.create(restriction, separator));
        Map<String, Object> paramMap = getParamMap();
        if (MapUtils.isNotEmpty(restriction.getParamMap())) {
            paramMap.putAll(restriction.getParamMap());
        }
        return this;
    }

    public static class RestrictionsBuilder {
        private ConditionAppender appender;
        RestrictionsBuilder() {
            this.appender = ConditionAppender.newInstance();
        }
        RestrictionsBuilder(ConditionAppender appender) {
            this.appender = appender;
        }

        public Restrictions build() {
            return new Restrictions(appender);
        }
        public String SQL() {
            return new Restrictions(appender).SQL();
        }

        public <T> Restrictions.RestrictionsBuilder eq(String name, T value) {
            appender.append(Condition.create(Restriction.eq(name, value)));
            return this;
        }
        public <T> Restrictions.RestrictionsBuilder notEq(String name, T value) {
            appender.append(Condition.create(Restriction.notEq(name, value)));
            return this;
        }
        public <T> Restrictions.RestrictionsBuilder isNull(String name) {
            appender.append(Condition.create(Restriction.isNull(name)));
            return this;
        }
        public <T> Restrictions.RestrictionsBuilder isNotNull(String name) {
            appender.append(Condition.create(Restriction.isNotNull(name)));
            return this;
        }
        public <T> Restrictions.RestrictionsBuilder in(String name, Collection<T> collection) {
            appender.append(Condition.create(Restriction.in(name, collection)));
            return this;
        }
        public <T> Restrictions.RestrictionsBuilder notIn(String name, Collection<T> collection) {
            appender.append(Condition.create(Restriction.notIn(name, collection)));
            return this;
        }
        public Restrictions.RestrictionsBuilder like(String name, String value) {
            appender.append(Condition.create(Restriction.like(name, value)));
            return this;
        }
        public Restrictions.RestrictionsBuilder likeBoth(String name, String value) {
            appender.append(Condition.create(Restriction.likeBoth(name, value)));
            return this;
        }
        public Restrictions.RestrictionsBuilder greaterEqual(String name, Object value) {
            appender.append(Condition.create(Restriction.greaterEqual(name, value)));
            return this;
        }
        public Restrictions.RestrictionsBuilder greaterThan(String name, Object value) {
            appender.append(Condition.create(Restriction.greaterThan(name, value)));
            return this;
        }
        public Restrictions.RestrictionsBuilder lessEqual(String name, Object value) {
            appender.append(Condition.create(Restriction.lessEqual(name, value)));
            return this;
        }
        public Restrictions.RestrictionsBuilder lessThan(String name, Object value) {
            appender.append(Condition.create(Restriction.lessThan(name, value)));
            return this;
        }

        public Restrictions.RestrictionsBuilder or(RestrictionUnit restrictions) {
            appender.append(Condition.create(restrictions, AbstractAppender.OR));
            return this;
        }
        public Restrictions.RestrictionsBuilder and(RestrictionUnit restriction) {
            appender.append(Condition.create(restriction, AbstractAppender.AND));
            return this;
        }
        public Restrictions.RestrictionsBuilder sql(String restriction) {
            appender.append(Condition.create(Restriction.sql(restriction)));
            return this;
        }


    }
}
