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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * This a utility to convert units
 *
 * Usage:
 * <pre>
 * {@code
 * double result = UsageConverter.convert(double amount, Unit unitfrom, Unit to);
 * }
 * </pre>
 *
 * Units are only convertable to other units in the same category (UnitType)
 *
 * @author fusionlightcat / Enveed / Arthur Schüler (C) 2017
 *
 * You are free to use this file in any project, as long you do not claim it as your own or remove this notice.
 */
public class UnitConverter {

    public enum Unit{

        //BASE UNITS
        KELVIN(UnitType.TEMPERATURE,"K",UnitSystem.METRIC,"Kelvin",new Aliases("°k","kelvin","k")),
        KILOGRAM(UnitType.MASS,"kg",UnitSystem.METRIC,"Kilogram",new Aliases("kilos","kilo","kilogram","kgs","kilograms","kg")),
        METRE(UnitType.LENGTH,"m",UnitSystem.METRIC, "Metre",new Aliases("meter","meters","metres","metre","m")),
        LITRE(UnitType.VOLUME,"l",UnitSystem.METRIC,"Litre",new Aliases("litre","litres","liter","liters","l")),
        SQ_METRE(UnitType.AREA,"m²",UnitSystem.METRIC,"Square metre",new Aliases("m²")),
        US_DOLLAR(UnitType.CURRENCY,"$",UnitSystem.IMPERIAL,"US Dollar",new Aliases("dollar","dollars","$s","usd","$")),
        SECONDS(UnitType.TIME,"s",UnitSystem.METRIC,"Seconds",new Aliases("seconds","secs","s","second")),

        XP(UnitType.DISCORD,"xp",UnitSystem.XP_SYS,"XP"),

        //Temperature
        FAHRENHEIT(UnitType.TEMPERATURE,"F",UnitSystem.IMPERIAL,"Fahrenheit",Unit.KELVIN, amount -> (5.0d/9.0d*(amount-32.0d)) + 273.0d,
                amount -> (9.0d/5.0d)*(amount-273.0d)+32.0d,new Aliases("°F","fahrenheit","farenheit","f")),
        CELSIUS(UnitType.TEMPERATURE, "C", UnitSystem.METRIC, "Celsius", Unit.KELVIN, amount -> amount + 273.15d, amount -> amount-273.15d,
                new Aliases("°C","celsius","c")),


        //MASS
        OUNCE(UnitType.MASS, "oz",UnitSystem.IMPERIAL, "Ounce", Unit.KILOGRAM, 0.0283495d, 35.274d, new Aliases("ounce","ounces","oz")),
        POUND(UnitType.MASS,"lb",UnitSystem.IMPERIAL,"Pound", Unit.KILOGRAM, 0.453592d, 2.20462d, new Aliases("lbs","lb")),
        STONE(UnitType.MASS,"st",UnitSystem.IMPERIAL,"Stone", Unit.KILOGRAM, 6.35029d, 0.157473d, new Aliases("stone","stones","sts","st")),
        IMPERIAL_TON(UnitType.MASS,"tn", UnitSystem.IMPERIAL,"Imperial Tonne", Unit.KILOGRAM, 0.157473d, 0.000984207d, new Aliases("tn")),

        GRAM(UnitType.MASS,"g",UnitSystem.METRIC, "Gram", Unit.KILOGRAM, 0.001d, 1000.0d, new Aliases("gram","grams","gramm","gramms","g")),
        TONNE(UnitType.MASS, "t", UnitSystem.METRIC, "Tonne", Unit.KILOGRAM, 1000.0d, 0.001d, new Aliases("ton","tons","tonnes","tns","t")),

        //LENGTH
        INCH(UnitType.LENGTH,"in",UnitSystem.IMPERIAL,"Inch", Unit.METRE, 0.0254d, 39.3701d, new Aliases("inch","inchs","inches")),
        FOOT(UnitType.LENGTH,"ft",UnitSystem.IMPERIAL,"Feet", Unit.METRE, 0.3048d, 3.28084d, new Aliases("foot","foots","ft","''")),
        YARD(UnitType.LENGTH,"yd",UnitSystem.IMPERIAL,"Yard", Unit.METRE, 0.9144d, 1.09361d, new Aliases("yard","yards","yd")),
        MILE(UnitType.LENGTH,"mi",UnitSystem.IMPERIAL,"Mile", Unit.METRE, 1609.34d, 0.000621371d, new Aliases("mile","ml","miles","mis","mi")),

        MILLIMETRE(UnitType.LENGTH,"mm",UnitSystem.METRIC,"Millimetre", Unit.METRE, 0.001d, 1000.0d, new Aliases("millis","millimetre","millimeter","millimeters","millimetres","mm")),
        CENTIMETRE(UnitType.LENGTH,"cm",UnitSystem.METRIC, "Centimetre", Unit.METRE, 0.01d, 100.0d, new Aliases("centis","centimetre","centimeter","centimetres","centimeters","cm")),
        KILOMETRE(UnitType.LENGTH,"km",UnitSystem.METRIC,"Kilometre", Unit.METRE, 1000.0d, 0.001d, new Aliases("kilometres","kilometers","kms","kilometre","kilometer","km")),

        //VOLUME
        FLUID_OUNCE(UnitType.VOLUME, "floz", UnitSystem.IMPERIAL, "Fluid ounce", Unit.LITRE, 0.0284131d, 35.1951d,new Aliases("floz","foz")),
        PINT(UnitType.VOLUME, "pt", UnitSystem.IMPERIAL, "Pint", Unit.LITRE, 0.568261d, 1.75975d, new Aliases("pint","pints","pts","pt")),
        QUART(UnitType.VOLUME, "qt", UnitSystem.IMPERIAL, "Quart", Unit.LITRE, 1.13652d, 0.879877d, new Aliases("quart","quarts","qt")),
        GALLON(UnitType.VOLUME, "gal", UnitSystem.IMPERIAL, "Gallon", Unit.LITRE, 4.54609d, 0.219969d, new Aliases("gallon","gallons","gal")),

        MILLILITRE(UnitType.VOLUME, "ml", UnitSystem.METRIC, "Millilitre", Unit.LITRE, 0.001d, 1000.0d,
                new Aliases("millilitres","milliliters","milliliter","millilitre","ml")),

        //AREA
        SQ_INCH(UnitType.AREA, "in²", UnitSystem.IMPERIAL, "Square inch", Unit.SQ_METRE, 0.00064516d, 1550.0d),
        SQ_FOOT(UnitType.AREA, "ft²", UnitSystem.IMPERIAL, "Square foot", Unit.SQ_METRE, 0.092903d, 10.7639d),
        SQ_MILE(UnitType.AREA, "ml²", UnitSystem.IMPERIAL, "Square mile", Unit.SQ_METRE, 2589988.0d, 0.0000003861d),
        SQ_YARD(UnitType.AREA, "yd²", UnitSystem.IMPERIAL, "Square yard", Unit.SQ_METRE, 0.836127d, 1.19599d),
        ACRE(UnitType.AREA, "ac", UnitSystem.IMPERIAL, "Acre", Unit.SQ_METRE, 4046.86d, 0.000247105d,
                new Aliases("acre","acres","ac")),

        HECTARE(UnitType.AREA, "ha", UnitSystem.METRIC, "Hectare", Unit.SQ_METRE, 1000.0d, 0.0001d,
                new Aliases("hectare","hectars","hectares","hectar")),
        SQ_KILOMETRE(UnitType.AREA, "km²",UnitSystem.METRIC, "Square kilometres", Unit.SQ_METRE, 1000000.0d, 0.000001d),

        //CURRENCY
        EURO(UnitType.CURRENCY, "€", UnitSystem.METRIC, "Euro", Unit.US_DOLLAR, amount -> amount * ExchangeRates.rateTo(Currency.EUR, Currency.USD), amount -> amount * ExchangeRates.rateTo(Currency.USD,Currency.EUR), new Aliases("euro", "euros", "eur", "€s","€")),
        GBP(UnitType.CURRENCY,"£",UnitSystem.METRIC, "Pound Sterling", Unit.US_DOLLAR, x -> x * ExchangeRates.rateTo(Currency.GBP,Currency.USD), x -> x * ExchangeRates.rateTo(Currency.USD, Currency.GBP), new Aliases("pounds","gbp","pound","£","₤")),
        AUD(UnitType.CURRENCY,"AUD",UnitSystem.METRIC, "Australian Dollar", Unit.US_DOLLAR, x -> x * ExchangeRates.rateTo(Currency.AUD,Currency.USD), x -> x * ExchangeRates.rateTo(Currency.USD, Currency.AUD), new Aliases("aud","audollars","a$","a$s")),
        CHF(UnitType.CURRENCY,"Fr",UnitSystem.METRIC, "Swiss francs", Unit.US_DOLLAR, x -> x * ExchangeRates.rateTo(Currency.CHF, Currency.USD), x -> x * ExchangeRates.rateTo(Currency.USD, Currency.CHF), new Aliases("francs","frances","fr","frs")),

        //TIME
        MILLISECONDS(UnitType.TIME,"ms",UnitSystem.METRIC,"Milliseconds",Unit.SECONDS, 0.001d,1000.0d,new Aliases("ms","millis","milliseconds","millisecs","ms")),
        MINUTES(UnitType.TIME,"min",UnitSystem.METRIC,"Minutes",Unit.SECONDS,60d,1d/60d, new Aliases("min","mins","minutes","minute","min")),
        HOURS(UnitType.TIME,"h",UnitSystem.METRIC,"Hours",Unit.SECONDS, 3600d, 1d/3600d, new Aliases("h","hours","hrs","hour","hr","h")),
        DAYS(UnitType.TIME,"d",UnitSystem.METRIC,"Days",Unit.SECONDS,86400d,1d/86400d, new Aliases("d","ds","days","day","d")),
        YEAR(UnitType.TIME,"y",UnitSystem.METRIC,"Years",Unit.SECONDS,31536000d, 1d/31536000d, new Aliases("y","year","years")),

        //DISCORD
        SMONG(UnitType.DISCORD,"smong",UnitSystem.XP_SYS, "Smong", Unit.XP,1000.0d,(1.0d/1000.0d),null),
        LSD(UnitType.DISCORD,"lsd",UnitSystem.XP_SYS,"LSD", Unit.XP, 25000.0d, (1.0d/25000.0d),null),
        MEME(UnitType.DISCORD,"meme",UnitSystem.XP_SYS, "Meme", Unit.XP, 100000.0d, (1.0d/100000.0d),null),
        NOOD(UnitType.DISCORD,"nood",UnitSystem.XP_SYS, "Nood", Unit.XP, 200000.0d, (1.0d/200000.0d),null),


        ;

        static {
            KILOGRAM.equivalent = POUND;
            METRE.equivalent = YARD;
            LITRE.equivalent = PINT;
            SQ_METRE.equivalent = SQ_FOOT;

            FAHRENHEIT.equivalent = CELSIUS;
            CELSIUS.equivalent = FAHRENHEIT;

            OUNCE.equivalent = GRAM;
            POUND.equivalent = KILOGRAM;
            STONE.equivalent = KILOGRAM;
            IMPERIAL_TON.equivalent = TONNE;
            GRAM.equivalent = OUNCE;
            TONNE.equivalent = IMPERIAL_TON;

            INCH.equivalent = CENTIMETRE;
            FOOT.equivalent = CENTIMETRE;
            YARD.equivalent = METRE;
            MILE.equivalent = KILOMETRE;
            MILLIMETRE.equivalent = INCH;
            CENTIMETRE.equivalent = INCH;
            KILOMETRE.equivalent = MILE;

            FLUID_OUNCE.equivalent = MILLILITRE;
            PINT.equivalent = LITRE;
            QUART.equivalent = LITRE;
            GALLON.equivalent = LITRE;
            MILLILITRE.equivalent = FLUID_OUNCE;

            SQ_INCH.equivalent = SQ_METRE;
            SQ_FOOT.equivalent = SQ_METRE;
            SQ_MILE.equivalent = SQ_KILOMETRE;
            SQ_YARD.equivalent = SQ_METRE;
            ACRE.equivalent = SQ_KILOMETRE;

            HECTARE.equivalent = SQ_FOOT;
            SQ_KILOMETRE.equivalent = SQ_MILE;

            EURO.equivalent = US_DOLLAR;
            US_DOLLAR.equivalent = EURO;
            GBP.equivalent = US_DOLLAR;
            CHF.equivalent = EURO;
            AUD.equivalent = US_DOLLAR;


            SECONDS.equivalent = MILLISECONDS;
            MILLISECONDS.equivalent = SECONDS;
            MINUTES.equivalent = SECONDS;
            HOURS.equivalent = MINUTES;
            DAYS.equivalent = HOURS;

        }

        private UnitType type;
        private String unit;
        private UnitSystem system;
        private String name;
        private Unit reference;
        private Converter to;
        private Converter from;
        private boolean base;
        private Aliases aliases;
        private Unit equivalent;

        /**
         * Creates a new Unit
         * @param type unit category
         * @param unit short name for this unit
         * @param system unit system to use (imperial/metric)
         * @param name unit name
         * @param reference the base unit of this category to convert to
         * @param multtoref the amount to multiply the amount with to convert it to the base unit
         */
        Unit(UnitType type, String unit, UnitSystem system, String name, Unit reference, double multtoref, double multfromref, Aliases aliases){
            this.type = type;
            this.unit = unit;
            this.system = system;
            this.name = name;
            this.reference = reference;
            this.to = new MultiplicationConverter(multtoref);
            this.from = new MultiplicationConverter(multfromref);
            base = false;
            this.aliases = aliases;
        }

        /**
         * Creates a new Unit
         * @param type unit category
         * @param unit short name for this unit
         * @param system unit system to use (imperial/metric)
         * @param name unit name
         * @param reference the base unit of this category to convert to
         * @param multtoref the amount to multiply the amount with to convert it to the base unit
         */
        Unit(UnitType type, String unit, UnitSystem system, String name, Unit reference, double multtoref, double multfromref){
            this.type = type;
            this.unit = unit;
            this.system = system;
            this.name = name;
            this.reference = reference;
            this.to = new MultiplicationConverter(multtoref);
            this.from = new MultiplicationConverter(multfromref);
            base = false;
            this.aliases = null;
        }

        /**
         * Creates a new Unit
         * @param type unit category
         * @param unit short name for this unit
         * @param system unit system to use (imperial/metric)
         * @param name unit name
         * @param reference the base unit of this category to convert to
         */
        Unit(UnitType type, String unit, UnitSystem system, String name, Unit reference, Converter converterto, Converter converterfrom){
            this.type = type;
            this.unit = unit;
            this.system = system;
            this.name = name;
            this.reference = reference;
            this.to = converterto;
            this.from = converterfrom;
            base = false;
            aliases = null;
        }

        /**
         * Creates a new Unit
         * @param type unit category
         * @param unit short name for this unit
         * @param system unit system to use (imperial/metric)
         * @param name unit name
         * @param reference the base unit of this category to convert to
         */
        Unit(UnitType type, String unit, UnitSystem system, String name, Unit reference, Converter converterto, Converter converterfrom, Aliases aliases){
            this.type = type;
            this.unit = unit;
            this.system = system;
            this.name = name;
            this.reference = reference;
            this.to = converterto;
            this.from = converterfrom;
            base = false;
            this.aliases = aliases;
        }

        /**
         * Creates a new Base Unit
         * @param type unit category
         * @param unit short name for this unit
         * @param system unit system to use (imperial/metric)
         * @param name unit name
         */
        Unit(UnitType type, String unit, UnitSystem system, String name){
            this.type = type;
            this.unit = unit;
            this.system = system;
            this.name = name;
            this.reference = null;
            this.to = null;
            this.from = null;
            base = true;
            aliases = null;
        }

        /**
         * Creates a new Base Unit
         * @param type unit category
         * @param unit short name for this unit
         * @param system unit system to use (imperial/metric)
         * @param name unit name
         */
        Unit(UnitType type, String unit, UnitSystem system, String name, Aliases aliases){
            this.type = type;
            this.unit = unit;
            this.system = system;
            this.name = name;
            this.reference = null;
            this.to = null;
            this.from = null;
            base = true;
            this.aliases = aliases;
        }

        public UnitType getType(){
            return type;
        }

        public String getSymbol() {
            return unit;
        }

        public UnitSystem getSystem() {
            return system;
        }

        public String getName() {
            return name;
        }

        public Unit getReference() {
            return reference;
        }

        public Converter getTo() {
            return to;
        }

        public Converter getFrom() {
            return from;
        }

        public boolean isBase() {
            return base;
        }

        public Aliases getAliases() {
            return aliases;
        }

        public static Unit getBySymbol(String sym){
            for(Unit unit : Unit.values()){
                if(unit.getSymbol().equalsIgnoreCase(sym)) return unit;
            }
            return null;
        }

        public static Collection<Unit> getByType(UnitType type){
            Collection<Unit> col = new ArrayList<>();
            for(Unit unit : Unit.values()){
                if(unit.getType().equals(type)) col.add(unit);
            }
            return col;
        }

        public static Unit getByAlias(String alias){
            for(Unit unit : Unit.values()){
                if(unit.hasAlias(alias.toLowerCase())) return unit;
            }
            return null;
        }

        public static Unit getByTyped(String typed){
            Unit unit = getBySymbol(typed);
            if(unit!=null) return unit;
            unit = getByAlias(typed);
            if(unit!=null) return unit;
            return null;
        }

        public boolean hasAlias(String al){
            if(aliases==null) return false;
            else return aliases.contains(al);
        }

        public Unit getEquivalent() {
            return equivalent;
        }
    }

    public enum UnitType {
        TEMPERATURE("Temperature"),
        LENGTH("Length"),
        AREA("Area"),
        VOLUME("Volume"),
        MASS("Mass"),
        CURRENCY("Currency"),
        TIME("Time"),
        DISCORD("Discord Unit"),
        ;

        private String name;

        UnitType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public enum UnitSystem {
        IMPERIAL("Imperial"),
        METRIC("Metric"),
        XP_SYS("XP System"),
        ;

        private String name;

        UnitSystem(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static class Aliases{

        String[] aliases;

        public Aliases(String... aliases) {
            this.aliases = aliases;
        }

        public String[] getAliases() {
            return aliases;
        }

        public Collection<String> getAliasesCollection() {
            return Arrays.asList(aliases);
        }

        public boolean contains(String text){
            return getAliasesCollection().contains(text);
        }
    }

    // METHODS

    /**
     * Convert a unit to another
     * @param amount the number
     * @param from the unit the number is in
     * @param to the unit the number should be converted to
     * @return the converted amount
     * @throws IncompatibleUnitTypesException when the units have different UnitTypes (are not from the same category)
     */
    public static double convert(double amount, Unit from, Unit to) throws IncompatibleUnitTypesException{

        if(!from.getType().equals(to.getType())){
            throw new IncompatibleUnitTypesException();
        }

        double based;
        if(from.isBase()){
            based = amount;
        }
        else {
            based = from.getTo().convert(amount);
        }

        double result;
        if(to.isBase()){
            result = based;
        }
        else {
            result = to.getFrom().convert(based);
        }

        return result;

    }

    // CONVERTERS

    private interface Converter {
        double convert(double amount);
    }

    private static class MultiplicationConverter implements Converter{

        private double fac;

        public MultiplicationConverter(double factor){
            this.fac = factor;
        }

        @Override
        public double convert(double amount) {
            return amount*fac;
        }

    }

    //EXCEPTION

    public static class IncompatibleUnitTypesException extends Exception{
        public IncompatibleUnitTypesException(String message) {
            super(message);
        }

        public IncompatibleUnitTypesException(){
            super();
        }
    }



}
