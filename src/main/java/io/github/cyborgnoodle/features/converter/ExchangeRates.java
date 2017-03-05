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

package io.github.cyborgnoodle.features.converter;

import io.github.cyborgnoodle.util.Log;
import javafx.util.Pair;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by arthur on 01.03.17.
 */
public class ExchangeRates {

    /**
     * Cache that currency!
     */
                            //curr       toUSD  fromUSD
    private static HashMap<Currency,Pair<Double,Double>> cache;

    static {
        cache = new HashMap<>();
    }

    public static void updateRates(){

        for(Currency currency : Currency.values()){

            try {
                Stock tousdstock = YahooFinance.get(currency.getCodeToUSD());
                Stock fromusdstock = YahooFinance.get(currency.getCodeFromUSD());

                double tousd = tousdstock.getQuote().getPrice().doubleValue();
                double fromusd = fromusdstock.getQuote().getPrice().doubleValue();

                cache.put(currency,new Pair<>(tousd,fromusd));

            } catch (IOException e) {
                Log.error("Failed to fetch exchange rates for "+currency.name()+"! "+e.getMessage());
                Log.stacktrace(e);
            }

        }

    }

    public static double rateTo(Currency currency, Currency other){

        double tousdprice = cache.getOrDefault(currency,new Pair<>(1d,1d)).getKey();
        double fromusdprice = cache.getOrDefault(other,new Pair<>(1d,1d)).getValue();

        return tousdprice * fromusdprice; //calculate the final rate
    }

}
