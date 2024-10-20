package com.norb.utils;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author <a href="mailto:tdnorbra@nps.edu?subject=com.norb.utils.UtilsTestSuite">Terry Norbraten, NPS MOVES</a>
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    com.norb.utils.FileIOTest.class,
    com.norb.utils.StreamCopierTest.class,
    com.norb.utils.StreamReaderTest.class,
    com.norb.utils.ProjectResourcesTest.class
})
public class UtilsTestSuite {}
