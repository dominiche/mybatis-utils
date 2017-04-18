package dominic.mybatis.support.stream;

import dominic.mybatis.support.ISupport;
import dominic.mybatis.support.Restriction;
import dominic.mybatis.support.UpdateField;
import dominic.mybatis.support.appender.RestrictionAppender;
import dominic.mybatis.support.appender.UpdateFieldAppender;

import java.util.Collection;

/**
 * Created by Administrator:herongxing on 2017/4/18 16:21.
 */
public class UpdateFields implements ISupport{

    private UpdateFieldAppender appender;

    UpdateFields(UpdateFieldAppender appender) {
        this.appender = appender;
    }

    public static UpdateFields.UpdateFieldsBuilder builder() {
        return new UpdateFields.UpdateFieldsBuilder();
    }

    @Override
    public String SQL() {
        return appender.SQL();
    }

    @Override
    public String toString() {
        return SQL();
    }

    public static class UpdateFieldsBuilder {
        private UpdateFieldAppender appender;
        UpdateFieldsBuilder() {
            this.appender = UpdateFieldAppender.newInstance();
        }

        public UpdateFields build() {
            return new UpdateFields(appender);
        }
        public String SQL() {
            return new UpdateFields(appender).SQL();
        }

        public <T> UpdateFields.UpdateFieldsBuilder set(String name, T value) {
            appender.append(UpdateField.set(name, value));
            return this;
        }
        public UpdateFields.UpdateFieldsBuilder setBySql(String field) {
            appender.append(UpdateField.setBySql(field));
            return this;
        }


    }
}
