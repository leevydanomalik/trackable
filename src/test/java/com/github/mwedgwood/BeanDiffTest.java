package com.github.mwedgwood;

import com.google.common.collect.Sets;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;

import static org.junit.Assert.*;

public class BeanDiffTest {

    @Test
    public void testDiff() throws Exception {

        TestBean oldValue = new TestBean()
                .setFieldOne("oldValue")
                .setFieldTwo("oldValue");

        TestBean newValue = new TestBean()
                .setFieldOne("newValue")
                .setFieldTwo("newValue");

        BeanDiff<TestBean> diff = new BeanDiff<>(oldValue, newValue);

        assertEquals(2, diff.getChangedFields().size());
        assertTrue(diff.getChangedFields().containsAll(Sets.newHashSet("fieldOne", "fieldTwo")));

        assertEquals("oldValue", diff.getOldValue("fieldOne"));
        assertEquals("oldValue", diff.getOldValue("fieldTwo"));

        assertEquals("newValue", diff.getNewValue("fieldOne"));
        assertEquals("newValue", diff.getNewValue("fieldTwo"));
    }

    @Test
    public void testDiffWithIncludedFields() throws Exception {

        TestBean oldValue = new TestBean()
                .setFieldOne("oldValue")
                .setFieldTwo("oldValue")
                .setFieldThree(new TestBean());

        TestBean newValue = new TestBean()
                .setFieldOne("newValue")
                .setFieldTwo("newValue")
                .setFieldThree(new TestBean());

        HashSet<String> includedFields = Sets.newHashSet("fieldOne");
        BeanDiff<TestBean> diff = new BeanDiff<>(oldValue, newValue, includedFields, Collections.emptySet());

        assertEquals(1, diff.getChangedFields().size());
        assertTrue(diff.getChangedFields().containsAll(includedFields));
        assertFalse(diff.getChangedFields().contains("fieldTwo"));
        assertFalse(diff.getChangedFields().contains("fieldThree"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDiffWithIncludedInvalidFieldName() throws Exception {

        HashSet<String> includedFields = Sets.newHashSet("foo", "fieldOne");
        new BeanDiff<>(new TestBean(), new TestBean(), includedFields, Collections.emptySet());
    }

    @Test
    public void testDiffWithExcludedFields() throws Exception {

        TestBean oldValue = new TestBean()
                .setFieldOne("oldValue")
                .setFieldTwo("oldValue")
                .setFieldThree(new TestBean());

        TestBean newValue = new TestBean()
                .setFieldOne("newValue")
                .setFieldTwo("newValue")
                .setFieldThree(new TestBean());

        HashSet<String> excludedFields = Sets.newHashSet("fieldOne");
        BeanDiff<TestBean> diff = new BeanDiff<>(oldValue, newValue, Collections.emptySet(), excludedFields);

        assertEquals(2, diff.getChangedFields().size());
        assertTrue(diff.getChangedFields().containsAll(Sets.newHashSet("fieldTwo", "fieldThree")));
        assertTrue(diff.getChangedFields().stream().noneMatch(excludedFields::contains));
    }

    @Test
    public void testDiffWithIncludedAndExcludedFields() throws Exception {

        TestBean oldValue = new TestBean()
                .setFieldOne("oldValue")
                .setFieldTwo("oldValue")
                .setFieldThree(new TestBean());

        TestBean newValue = new TestBean()
                .setFieldOne("newValue")
                .setFieldTwo("newValue")
                .setFieldThree(new TestBean());

        HashSet<String> includedFields = Sets.newHashSet("fieldOne", "fieldTwo");
        HashSet<String> excludedFields = Sets.newHashSet("fieldOne", "fieldTwo");
        BeanDiff<TestBean> diff = new BeanDiff<>(oldValue, newValue, includedFields, excludedFields);

        assertEquals(2, diff.getChangedFields().size());
        assertTrue(diff.getChangedFields().containsAll(includedFields));
    }

    @Test
    public void testAssertValidFieldWhenValid() throws Exception {
        try {
            new BeanDiff<>(new TestBean(), new TestBean()).assertValidField("fieldOne");
        } catch (Exception e) {
            fail("Should not have thrown and exception");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAssertValidFieldWhenInvalid() throws Exception {
        new BeanDiff<>(new TestBean(), new TestBean()).assertValidField("fooBar");
    }

}
