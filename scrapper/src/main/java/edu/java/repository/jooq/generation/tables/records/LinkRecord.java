/*
 * This file is generated by jOOQ.
 */

package edu.java.repository.jooq.generation.tables.records;


import edu.java.repository.jooq.generation.tables.Link;
import jakarta.validation.constraints.Size;
import java.beans.ConstructorProperties;
import java.time.OffsetDateTime;
import javax.annotation.processing.Generated;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record7;
import org.jooq.Row7;
import org.jooq.impl.UpdatableRecordImpl;


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
public class LinkRecord extends UpdatableRecordImpl<LinkRecord> implements Record7<Long, String, OffsetDateTime, OffsetDateTime, OffsetDateTime, Integer, String> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>LINK.LINK_ID</code>.
     */
    public void setLinkId(@Nullable Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>LINK.LINK_ID</code>.
     */
    @Nullable
    public Long getLinkId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>LINK.LINK_NAME</code>.
     */
    public void setLinkName(@Nullable String value) {
        set(1, value);
    }

    /**
     * Getter for <code>LINK.LINK_NAME</code>.
     */
    @Size(max = 511)
    @Nullable
    public String getLinkName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>LINK.LAST_CHECK</code>.
     */
    public void setLastCheck(@Nullable OffsetDateTime value) {
        set(2, value);
    }

    /**
     * Getter for <code>LINK.LAST_CHECK</code>.
     */
    @Nullable
    public OffsetDateTime getLastCheck() {
        return (OffsetDateTime) get(2);
    }

    /**
     * Setter for <code>LINK.LAST_UPDATE</code>.
     */
    public void setLastUpdate(@Nullable OffsetDateTime value) {
        set(3, value);
    }

    /**
     * Getter for <code>LINK.LAST_UPDATE</code>.
     */
    @Nullable
    public OffsetDateTime getLastUpdate() {
        return (OffsetDateTime) get(3);
    }

    /**
     * Setter for <code>LINK.LAST_COMMIT</code>.
     */
    public void setLastCommit(@Nullable OffsetDateTime value) {
        set(4, value);
    }

    /**
     * Getter for <code>LINK.LAST_COMMIT</code>.
     */
    @Nullable
    public OffsetDateTime getLastCommit() {
        return (OffsetDateTime) get(4);
    }

    /**
     * Setter for <code>LINK.AMOUNT_ISSUES</code>.
     */
    public void setAmountIssues(@Nullable Integer value) {
        set(5, value);
    }

    /**
     * Getter for <code>LINK.AMOUNT_ISSUES</code>.
     */
    @Nullable
    public Integer getAmountIssues() {
        return (Integer) get(5);
    }

    /**
     * Setter for <code>LINK.TYPE</code>.
     */
    public void setType(@Nullable String value) {
        set(6, value);
    }

    /**
     * Getter for <code>LINK.TYPE</code>.
     */
    @Size(max = 31)
    @Nullable
    public String getType() {
        return (String) get(6);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record7 type implementation
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Row7<Long, String, OffsetDateTime, OffsetDateTime, OffsetDateTime, Integer, String> fieldsRow() {
        return (Row7) super.fieldsRow();
    }

    @Override
    @NotNull
    public Row7<Long, String, OffsetDateTime, OffsetDateTime, OffsetDateTime, Integer, String> valuesRow() {
        return (Row7) super.valuesRow();
    }

    @Override
    @NotNull
    public Field<Long> field1() {
        return Link.LINK.LINK_ID;
    }

    @Override
    @NotNull
    public Field<String> field2() {
        return Link.LINK.LINK_NAME;
    }

    @Override
    @NotNull
    public Field<OffsetDateTime> field3() {
        return Link.LINK.LAST_CHECK;
    }

    @Override
    @NotNull
    public Field<OffsetDateTime> field4() {
        return Link.LINK.LAST_UPDATE;
    }

    @Override
    @NotNull
    public Field<OffsetDateTime> field5() {
        return Link.LINK.LAST_COMMIT;
    }

    @Override
    @NotNull
    public Field<Integer> field6() {
        return Link.LINK.AMOUNT_ISSUES;
    }

    @Override
    @NotNull
    public Field<String> field7() {
        return Link.LINK.TYPE;
    }

    @Override
    @Nullable
    public Long component1() {
        return getLinkId();
    }

    @Override
    @Nullable
    public String component2() {
        return getLinkName();
    }

    @Override
    @Nullable
    public OffsetDateTime component3() {
        return getLastCheck();
    }

    @Override
    @Nullable
    public OffsetDateTime component4() {
        return getLastUpdate();
    }

    @Override
    @Nullable
    public OffsetDateTime component5() {
        return getLastCommit();
    }

    @Override
    @Nullable
    public Integer component6() {
        return getAmountIssues();
    }

    @Override
    @Nullable
    public String component7() {
        return getType();
    }

    @Override
    @Nullable
    public Long value1() {
        return getLinkId();
    }

    @Override
    @Nullable
    public String value2() {
        return getLinkName();
    }

    @Override
    @Nullable
    public OffsetDateTime value3() {
        return getLastCheck();
    }

    @Override
    @Nullable
    public OffsetDateTime value4() {
        return getLastUpdate();
    }

    @Override
    @Nullable
    public OffsetDateTime value5() {
        return getLastCommit();
    }

    @Override
    @Nullable
    public Integer value6() {
        return getAmountIssues();
    }

    @Override
    @Nullable
    public String value7() {
        return getType();
    }

    @Override
    @NotNull
    public LinkRecord value1(@Nullable Long value) {
        setLinkId(value);
        return this;
    }

    @Override
    @NotNull
    public LinkRecord value2(@Nullable String value) {
        setLinkName(value);
        return this;
    }

    @Override
    @NotNull
    public LinkRecord value3(@Nullable OffsetDateTime value) {
        setLastCheck(value);
        return this;
    }

    @Override
    @NotNull
    public LinkRecord value4(@Nullable OffsetDateTime value) {
        setLastUpdate(value);
        return this;
    }

    @Override
    @NotNull
    public LinkRecord value5(@Nullable OffsetDateTime value) {
        setLastCommit(value);
        return this;
    }

    @Override
    @NotNull
    public LinkRecord value6(@Nullable Integer value) {
        setAmountIssues(value);
        return this;
    }

    @Override
    @NotNull
    public LinkRecord value7(@Nullable String value) {
        setType(value);
        return this;
    }

    @Override
    @NotNull
    public LinkRecord values(@Nullable Long value1, @Nullable String value2, @Nullable OffsetDateTime value3, @Nullable OffsetDateTime value4, @Nullable OffsetDateTime value5, @Nullable Integer value6, @Nullable String value7) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached LinkRecord
     */
    public LinkRecord() {
        super(Link.LINK);
    }

    /**
     * Create a detached, initialised LinkRecord
     */
    @ConstructorProperties({ "linkId", "linkName", "lastCheck", "lastUpdate", "lastCommit", "amountIssues", "type" })
    public LinkRecord(@Nullable Long linkId, @Nullable String linkName, @Nullable OffsetDateTime lastCheck, @Nullable OffsetDateTime lastUpdate, @Nullable OffsetDateTime lastCommit, @Nullable Integer amountIssues, @Nullable String type) {
        super(Link.LINK);

        setLinkId(linkId);
        setLinkName(linkName);
        setLastCheck(lastCheck);
        setLastUpdate(lastUpdate);
        setLastCommit(lastCommit);
        setAmountIssues(amountIssues);
        setType(type);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised LinkRecord
     */
    public LinkRecord(edu.java.repository.jooq.generation.tables.pojos.Link value) {
        super(Link.LINK);

        if (value != null) {
            setLinkId(value.getLinkId());
            setLinkName(value.getLinkName());
            setLastCheck(value.getLastCheck());
            setLastUpdate(value.getLastUpdate());
            setLastCommit(value.getLastCommit());
            setAmountIssues(value.getAmountIssues());
            setType(value.getType());
            resetChangedOnNotNull();
        }
    }
}