package com.github.mwedgwood;

import org.junit.Test;

import static org.junit.Assert.*;

public class TrackableImplTest {

    @Test
    public void testHasChanges() throws Exception {

        TestBean testBean = new TestBean()
                .setFieldOne("oldValueOne")
                .setFieldTwo("oldValueTwo")
                .setFieldThree(new TestBean());

        Trackable<TestBean> trackable = Trackable.track(testBean);

        testBean.setFieldTwo("newValueTwo");

        assertTrue("should have changes", trackable.hasChanges());
        assertEquals(1, trackable.diff().getChangedFields().size());
        assertEquals("oldValueTwo", trackable.diff().getOldValue("fieldTwo"));
        assertEquals("newValueTwo", trackable.diff().getNewValue("fieldTwo"));
    }

    @Test
    public void testWatching() throws Exception {

        TestBean testBean = new TestBean()
                .setFieldOne("oldValueOne")
                .setFieldTwo("oldValueTwo")
                .setFieldThree(new TestBean());

        Trackable<TestBean> trackable = Trackable.track(testBean).watching("fieldOne");

        testBean.setFieldTwo("newValue");
        assertFalse("should have no changes as we are watching fieldOne", trackable.hasChanges());

        testBean.setFieldOne("newValue");
        assertTrue("should have changes", trackable.hasChanges());
    }

    @Test
    public void testIgnoring() throws Exception {

        TestBean testBean = new TestBean()
                .setFieldOne("oldValueOne")
                .setFieldTwo("oldValueTwo")
                .setFieldThree(new TestBean());

        Trackable<TestBean> trackable = Trackable.track(testBean).ignoring("fieldOne");

        testBean.setFieldOne("newValue");
        assertFalse("should have no changes as we are ignoring fieldOne", trackable.hasChanges());

        testBean.setFieldTwo("newValue");
        assertTrue("should have changes", trackable.hasChanges());
    }
}
