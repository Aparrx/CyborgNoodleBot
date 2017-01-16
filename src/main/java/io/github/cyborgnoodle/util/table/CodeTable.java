/*
 * Copyright 2017 Enveed / Arthur Schüler
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.cyborgnoodle.util.table;

import io.github.cyborgnoodle.util.StringUtils;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

/**
 *
 */
public class CodeTable {

    private int[] columwidths;
    private boolean[] rightbound;
    private List<String> entries;

    public CodeTable(int... widths){
       columwidths = widths;
       entries = new ArrayList<>();
       rightbound = new boolean[widths.length]; //defaults to false
    }

    public void setRightBound(boolean... booleans){
        if(columwidths.length!=booleans.length) throw new IndexOutOfBoundsException("Specified too much or to few arguments to fit table size!");
        rightbound = booleans;
    }

    public void addRow(String... strings){
        String row = generateRow(strings);
        entries.add(row);
    }

    public void addRow(int index, String... strings){
        String row = generateRow(strings);
        entries.add(index,row);
    }

    private String generateRow(String... strings){

        if(columwidths.length!=strings.length) throw new IndexOutOfBoundsException("Specified too much or to few arguments to fit table size!");
        Formatter formatter = new Formatter();

        String format = "";

        int index = 0;
        for(int length : columwidths){
            boolean right = rightbound[index];
            String rs;
            if(right) rs = "";
            else rs = "-";
            format = format + "%" + rs + length + "s";
            index++;
        }
        String[] ellipsed = ellipsizeStrings(strings);
        return formatter.format(format,ellipsed).toString();

    }

    private String[] ellipsizeStrings(String... strings){
        if(columwidths.length!=strings.length) throw new IndexOutOfBoundsException("Specified too much or to few arguments to fit table size!");
        List<String> ellipsed = new ArrayList<>();
        int index = 0;
        for(String str : strings){
            int length = columwidths[index];
            String el = StringUtils.ellipsize(str,length-1);
            ellipsed.add(el);
            index++;
        }
        return ellipsed.toArray(new String[ellipsed.size()]);
    }

    public String asRaw(){
        String s = "";
        for(String row : entries) s = s + row+"\n";
        return s;
    }

    public String asCodeBlock(){
        return "```\n"+asRaw()+"\n```";
    }

}
