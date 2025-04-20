package lol.aabss.skuishy.elements.general;

import ch.njol.skript.lang.function.Parameter;
import ch.njol.skript.lang.function.SimpleJavaFunction;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.registrations.DefaultClasses;
import org.bukkit.util.EulerAngle;
import org.jetbrains.annotations.NotNull;

import static ch.njol.skript.lang.function.Functions.registerFunction;

@SuppressWarnings("NullableProblems")
public class Functions {

    static {
        Parameter<?>[] numbersParam = new Parameter[]{new Parameter<>("ns", DefaultClasses.NUMBER, false, null)};
        registerFunction(new SimpleJavaFunction<>("mean", numbersParam, DefaultClasses.NUMBER, true) {
            @Override
            public Number @NotNull [] executeSimple(Object[][] params) {
                Object[] ns = params[0];
                double sum = 0.0;
                for (Object num : ns) {
                    sum += ((Number) num).doubleValue();
                }
                double mean = sum / ns.length;
                return new Number[]{mean};
            }
        }
                .description("Calculates the mean of a list of numbers.")
                .since("1.7"));

        registerFunction(new SimpleJavaFunction<>("eulerangle", new Parameter[]{
                new Parameter<>("x", DefaultClasses.NUMBER, true, null),
                new Parameter<>("y", DefaultClasses.NUMBER, true, null),
                new Parameter<>("z", DefaultClasses.NUMBER, true, null)
        }, Classes.getExactClassInfo(EulerAngle.class), true) {
            @Override
            public EulerAngle[] executeSimple(Object[][] params) {
                if (params.length < 3) {
                    return null;
                }
                return new EulerAngle[]{new EulerAngle((Double) params[0][0], (Double) params[1][0], (Double) params[2][0])};
            }
        });

    }
}
