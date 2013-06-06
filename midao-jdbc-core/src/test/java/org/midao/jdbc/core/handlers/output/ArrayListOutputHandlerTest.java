/*
 * Copyright 2013 Zakhar Prykhoda
 *
 *    midao.org
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.midao.jdbc.core.handlers.output;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.midao.jdbc.core.handlers.output.ArrayListOutputHandler;

import java.util.List;

/**
 */
public class ArrayListOutputHandlerTest extends BaseOutputHandlerTest {

    @Before
    public void setUp() {
        init();
    }

    @Test
    public void testHandle() {
        List<Object[]> result = new ArrayListOutputHandler().handle(paramsList);

        Assert.assertArrayEquals(new Object[]{"jack", "sheriff", 36}, result.get(0));
        Assert.assertArrayEquals(new Object[]{"henry", "mechanic", 36}, result.get(1));
        Assert.assertArrayEquals(new Object[]{"alison", "agent", 30}, result.get(2));
    }

    @Test
    public void testEmpty() {
        List<Object[]> result = new ArrayListOutputHandler().handle(emptyList);

        Assert.assertEquals(0, result.size());
    }
}
