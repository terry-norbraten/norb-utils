package com.norb.utils;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author <a href="mailto:tdnorbra@nps.edu?subject=com.norb.utils.UtilsTestSuite">Terry Norbraten, NPS MOVES</a>
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    FileIOTest.class,
    DateTimeGroupStampTest.class,
    ProjectResourcesTest.class,
    StreamCopierTest.class,
    StreamReaderTest.class
})
public class UtilsTestSuite {}
