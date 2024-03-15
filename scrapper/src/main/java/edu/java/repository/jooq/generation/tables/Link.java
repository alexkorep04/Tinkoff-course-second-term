/*
 * This file is generated by jOOQ.
 */

package edu.java.repository.jooq.generation.tables;


import edu.java.repository.jooq.generation.DefaultSchema;
import edu.java.repository.jooq.generation.Keys;
import edu.java.repository.jooq.generation.tables.records.LinkRecord;
import java.time.OffsetDateTime;
import java.util.function.Function;
import javax.annotation.processing.Generated;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function7;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row7;
import org.jooq.Schema;
import org.jooq.SelectField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.18.9"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class Link extends TableImpl<LinkRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>LINK</code>
     */
    public static final Link LINK = new Link();

    /**
     * The class holding records for this type
     */
    @Override
    @NotNull
    public Class<LinkRecord> getRecordType() {
        return LinkRecord.class;
    }

    /**
     * The column <code>LINK.LINK_ID</code>.
     */
    public final TableField<LinkRecord, Long> LINK_ID = createField(DSL.name("link_id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>LINK.LINK_NAME</code>.
     */
    public final TableField<LinkRecord, String> LINK_NAME = createField(DSL.name("link_name"), SQLDataType.VARCHAR(511), this, "");

    /**
     * The column <code>LINK.LAST_CHECK</code>.
     */
    public final TableField<LinkRecord, OffsetDateTime> LAST_CHECK = createField(DSL.name("last_check"), SQLDataType.OFFSETDATETIME(6), this, "");

    /**
     * The column <code>LINK.LAST_UPDATE</code>.
     */
    public final TableField<LinkRecord, OffsetDateTime> LAST_UPDATE = createField(DSL.name("last_update"), SQLDataType.OFFSETDATETIME(6), this, "");

    /**
     * The column <code>LINK.LAST_COMMIT</code>.
     */
    public final TableField<LinkRecord, OffsetDateTime> LAST_COMMIT = createField(DSL.name("last_commit"), SQLDataType.OFFSETDATETIME(6), this, "");

    /**
     * The column <code>LINK.AMOUNT_ISSUES</code>.
     */
    public final TableField<LinkRecord, Integer> AMOUNT_ISSUES = createField(DSL.name("amount_issues"), SQLDataType.INTEGER.defaultValue(DSL.field(DSL.raw("-1"), SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>LINK.TYPE</code>.
     */
    public final TableField<LinkRecord, String> TYPE = createField(DSL.name("type"), SQLDataType.VARCHAR(31), this, "");

    private Link(Name alias, Table<LinkRecord> aliased) {
        this(alias, aliased, null);
    }

    private Link(Name alias, Table<LinkRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>LINK</code> table reference
     */
    public Link(String alias) {
        this(DSL.name(alias), LINK);
    }

    /**
     * Create an aliased <code>LINK</code> table reference
     */
    public Link(Name alias) {
        this(alias, LINK);
    }

    /**
     * Create a <code>LINK</code> table reference
     */
    public Link() {
        this(DSL.name("link"), null);
    }

    public <O extends Record> Link(Table<O> child, ForeignKey<O, LinkRecord> key) {
        super(child, key, LINK);
    }

    @Override
    @Nullable
    public Schema getSchema() {
        return aliased() ? null : DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    @NotNull
    public Identity<LinkRecord, Long> getIdentity() {
        return (Identity<LinkRecord, Long>) super.getIdentity();
    }

    @Override
    @NotNull
    public UniqueKey<LinkRecord> getPrimaryKey() {
        return Keys.CONSTRAINT_2;
    }

    @Override
    @NotNull
    public Link as(String alias) {
        return new Link(DSL.name(alias), this);
    }

    @Override
    @NotNull
    public Link as(Name alias) {
        return new Link(alias, this);
    }

    @Override
    @NotNull
    public Link as(Table<?> alias) {
        return new Link(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public Link rename(String name) {
        return new Link(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public Link rename(Name name) {
        return new Link(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    @NotNull
    public Link rename(Table<?> name) {
        return new Link(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row7 type methods
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Row7<Long, String, OffsetDateTime, OffsetDateTime, OffsetDateTime, Integer, String> fieldsRow() {
        return (Row7) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function7<? super Long, ? super String, ? super OffsetDateTime, ? super OffsetDateTime, ? super OffsetDateTime, ? super Integer, ? super String, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function7<? super Long, ? super String, ? super OffsetDateTime, ? super OffsetDateTime, ? super OffsetDateTime, ? super Integer, ? super String, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
