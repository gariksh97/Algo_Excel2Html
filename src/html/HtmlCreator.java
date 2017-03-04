package html;

import j2html.tags.ContainerTag;
import javafx.util.Pair;
import member.Member;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static j2html.TagCreator.*;

/**
 * Created by garik on 28.02.17.
 */
public class HtmlCreator {
    public static String htmlCreator(
            List<Pair<String, String>> headList,
            List<Member> bodyList,
            String name, String time
    ) {
        ContainerTag head = head().with(
                meta().
                        attr("http-equiv", "Content-Type").
                        attr("content", "text/html; charset=windows-1251"),
                title().withText("Результаты"),
                link().
                        withRel("stylesheet").
                        withHref("https://pcms.university.innopolis.ru/css/standings2.css")
        );
        ContainerTag table = table().withClass("standings");

        headList.removeAll(Collections.singleton(null));
        bodyList.removeAll(Collections.singleton(null));

        remove(headList, "place");
        remove(headList, "class");

        List<String> bodySkip = Arrays.asList("place", "class");

        ContainerTag tableTHead = thead().with(tr().with(
                headList.stream().map(strings ->
                        th().withClass(strings.getKey()).withText(strings.getValue())
                ).collect(Collectors.toList())
        ));
        final int[] value = new int[3];
        value[0] = Integer.parseInt(bodyList.get(0).get("solved"));
        value[1] = 0;
        value[2] = 0;
        ContainerTag tableTBody = tbody().with(
                bodyList.stream().map(member -> {
                    if (value[0] != Integer.parseInt(member.get("solved")) / 100) {
                        value[0] = Integer.parseInt(member.get("solved")) / 100;
                        value[1] = value[1] == 0 ? 1 : 0;
                    }
                    ContainerTag tr = tr().withClass(
                            "row" +
                                    value[1] +
                                    value[2]
                    );
                    value[2] = value[2] == 0 ? 1 : 0;
                    Member.forOrder(tag -> {
                                if (bodySkip.contains(tag)) return;
                                if (member.get(tag) == null) return;
                                if (tag.equals("school")) {
                                    union("school", "place", "party", tr, member);
                                    return;
                                }
                                if (tag.equals("party")) {
                                    union("party", "class", "party", tr, member, s -> {
                                        String split[] = s.split(",");
                                        return !(split.length == 1 || HtmlTagCreator.getNumber(s.split(",")[1]) == -1);
                                    });
                                    return;
                                }
                                tr.with(
                                        td().withClass(tag).withText(member.get(tag))
                                );
                            },
                            num -> {
                                String number = member.get(num).trim();
                                ContainerTag numTag;
                                if (number.equals(".") || number.equals("") || number.equals("-")) {
                                    tr.with(td().withClass("ioiprob").withText("."));
                                    return;
                                }
                                switch (Integer.parseInt(number)) {
                                    case 100:
                                    case 101:
                                        numTag = i().withText(number);
                                        break;
                                    case 0:
                                        numTag = b().withText(number);
                                        break;
                                    default:
                                        numTag = u().withText(number);
                                }
                                tr.with(td().withClass("ioiprob").with(numTag));
                            },
                            s -> member.get(s) != null
                    );
                    return tr;
                }).collect(Collectors.toList())
        );
        ContainerTag tableTFoot = tfoot();
        table.with(tableTHead);
        table.with(tableTBody);

        ContainerTag body = body().with(
                table().withClass("wrapper").with(
                        tbody().with(tr().with(td().with(
                                tag("center").with(
                                        a().withClass(URLEncoder.encode(name)).with(
                                                h2(name)
                                        ),
                                        p().withText(time + " из " + time).with(br()).withText("Статус окончен"),
                                        table
                                ))
                                )
                        )
                )
        );
        return html().with(head, body).render();

    }

    private static void remove(List<Pair<String, String>> list, String key) {
        Pair<String, String> toRemove = HtmlTagCreator.findByKey(list, key);
        if (toRemove != null)
            list.remove(toRemove);
    }

    private static void union(
            String tag1, String tag2, String className,
            ContainerTag tr, Member member
    ) {
        union(tag1, tag2, className, tr, member, null);
    }

    private static void union(
            String tag1, String tag2, String className,
            ContainerTag tr, Member member,
            Function<String, Boolean> additional
    ) {
        tr.with(
                td().withClass(className).withText(
                        member.get(tag1).trim() + (
                                member.get(tag2) == null || (additional != null) && additional.apply(member.get(tag1)) ?
                                        "" :
                                        ", " + member.get(tag2).trim()
                        )
                )
        );
    }

}
