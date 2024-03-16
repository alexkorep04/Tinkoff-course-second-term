package edu.java.repository.jooq;

import edu.java.dto.Link;
import edu.java.repository.LinkRepository;
import edu.java.repository.jooq.generation.Tables;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class JooqLinkRepository implements LinkRepository {
    private final DSLContext dslContext;

    @Override
    @Transactional
    public Link add(long chatId, String linkName) {
        Optional<Link> link = findByName(linkName);
        String type;
        if (linkName.startsWith("https://github.com/")) {
            type = "GitHubLink";
        } else {
            type = "StackOverflowLink";
        }
        long id = link.map(Link::getId).orElseGet(() -> dslContext.insertInto(Tables.LINK)
                .set(Tables.LINK.LINK_NAME, linkName)
                .set(Tables.LINK.TYPE, type)
                .set(Tables.LINK.LAST_CHECK, OffsetDateTime.now())
                .returning(Tables.LINK.LINK_ID)
                .fetchOne()
                .getLinkId());
        dslContext
            .insertInto(Tables.CHAT_LINK, Tables.CHAT_LINK.CHAT_ID, Tables.CHAT_LINK.LINK_ID)
            .values(chatId, id)
            .execute();
        return findById(id).get();
    }

    @Override
    @Transactional
    public Link remove(long chatId, String linkName) {
        Optional<Link> link = findByChatIdAndUrl(chatId, linkName);
        long id = link.get().getId();
        dslContext
            .deleteFrom(Tables.CHAT_LINK)
            .where(Tables.CHAT_LINK.CHAT_ID.eq(chatId))
            .and(Tables.CHAT_LINK.LINK_ID.eq(id))
            .execute();
        List<Long> chats = findChatsByLink(linkName);
        if (chats.isEmpty()) {
            dslContext
                .deleteFrom(Tables.LINK)
                .where(Tables.LINK.LINK_ID.eq(id))
                .execute();
        }
        return link.get();
    }

    @Override
    public List<Link> findAllLinksById(long chatId) {
        return dslContext
            .selectFrom(Tables.LINK)
            .where(Tables.LINK.LINK_ID.in(
                dslContext
                    .select(Tables.CHAT_LINK.LINK_ID)
                    .from(Tables.CHAT_LINK)
                    .where(Tables.CHAT_LINK.CHAT_ID.eq(chatId))
                    .fetchInto(Long.class)
            ))
            .fetch()
            .map(r -> new Link(
                r.get(Tables.LINK.LINK_ID),
                r.get(Tables.LINK.LINK_NAME),
                r.get(Tables.LINK.LAST_CHECK),
                r.get(Tables.LINK.LAST_UPDATE),
                r.get(Tables.LINK.LAST_COMMIT),
                r.get(Tables.LINK.AMOUNT_ISSUES),
                r.get(Tables.LINK.TYPE)
            ));
    }

    @Override
    public List<Link> findAll() {
        return dslContext.selectFrom(Tables.LINK)
            .fetch()
            .map(r -> new Link(
                r.get(Tables.LINK.LINK_ID),
                r.get(Tables.LINK.LINK_NAME),
                r.get(Tables.LINK.LAST_CHECK),
                r.get(Tables.LINK.LAST_UPDATE),
                r.get(Tables.LINK.LAST_COMMIT),
                r.get(Tables.LINK.AMOUNT_ISSUES),
                r.get(Tables.LINK.TYPE)
            ));
    }

    @Override
    public Optional<Link> findByChatIdAndUrl(Long chatId, String linkName) {
        List<Link> links = dslContext
            .selectDistinct()
            .from(Tables.CHAT_LINK)
            .join(Tables.LINK)
            .on(Tables.CHAT_LINK.LINK_ID.eq(Tables.LINK.LINK_ID))
            .where(Tables.CHAT_LINK.CHAT_ID.eq(chatId))
            .and(Tables.LINK.LINK_NAME.eq(linkName))
            .fetch()
            .map(r -> new Link(
                r.get(Tables.LINK.LINK_ID),
                r.get(Tables.LINK.LINK_NAME),
                r.get(Tables.LINK.LAST_CHECK),
                r.get(Tables.LINK.LAST_UPDATE),
                r.get(Tables.LINK.LAST_COMMIT),
                r.get(Tables.LINK.AMOUNT_ISSUES),
                r.get(Tables.LINK.TYPE)
            ));
        if (links.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(links.getFirst());
    }

    private Optional<Link> findByName(String name) {
        List<Link> links = dslContext
            .selectFrom(Tables.LINK)
            .where(Tables.LINK.LINK_NAME.eq(name))
            .fetch()
            .map(r -> new Link(
                r.get(Tables.LINK.LINK_ID),
                r.get(Tables.LINK.LINK_NAME),
                r.get(Tables.LINK.LAST_CHECK),
                r.get(Tables.LINK.LAST_UPDATE),
                r.get(Tables.LINK.LAST_COMMIT),
                r.get(Tables.LINK.AMOUNT_ISSUES),
                r.get(Tables.LINK.TYPE)
            ));
        if (links.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(links.getFirst());
    }

    @Override
    public Optional<Link> findById(long linkId) {
        List<Link> links = dslContext
            .selectFrom(Tables.LINK)
            .where(Tables.LINK.LINK_ID.eq(linkId))
            .fetch()
            .map(r -> new Link(
                r.get(Tables.LINK.LINK_ID),
                r.get(Tables.LINK.LINK_NAME),
                r.get(Tables.LINK.LAST_CHECK),
                r.get(Tables.LINK.LAST_UPDATE),
                r.get(Tables.LINK.LAST_COMMIT),
                r.get(Tables.LINK.AMOUNT_ISSUES),
                r.get(Tables.LINK.TYPE)
            ));
        if (links.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(links.getFirst());
    }

    @Override
    @Transactional
    public void updateLastCheck(OffsetDateTime newCheck, String name) {
        dslContext
            .update(Tables.LINK)
            .set(Tables.LINK.LAST_CHECK, newCheck)
            .where(Tables.LINK.LINK_NAME.eq(name))
            .execute();
    }

    @Override
    @Transactional
    public void updateLastUpdate(OffsetDateTime update, String name) {
        dslContext
            .update(Tables.LINK)
            .set(Tables.LINK.LAST_UPDATE, update)
            .where(Tables.LINK.LINK_NAME.eq(name))
            .execute();
    }

    @Override
    @Transactional
    public void updateLastCommit(OffsetDateTime commit, String name) {
        dslContext
            .update(Tables.LINK)
            .set(Tables.LINK.LAST_COMMIT, commit)
            .where(Tables.LINK.LINK_NAME.eq(name))
            .execute();
    }

    @Override
    @Transactional
    public void updateAmountOfIssues(int amountOfPR, String name) {
        dslContext
            .update(Tables.LINK)
            .set(Tables.LINK.AMOUNT_ISSUES, amountOfPR)
            .where(Tables.LINK.LINK_NAME.eq(name))
            .execute();
    }

    @Override
    public List<Long> findChatsByLink(String name) {
        return dslContext
            .selectDistinct(Tables.CHAT_LINK.CHAT_ID)
            .from(Tables.CHAT_LINK)
            .join(Tables.LINK)
            .on(Tables.CHAT_LINK.LINK_ID.eq(Tables.LINK.LINK_ID))
            .where(Tables.LINK.LINK_NAME.eq(name))
            .fetchInto(Long.class);
    }

    @Override
    public List<Link> findOldestLinks(int amount) {
        return dslContext
            .selectFrom(Tables.LINK)
            .orderBy(Tables.LINK.LAST_CHECK)
            .limit(amount)
            .fetch()
            .map(r -> new Link(
                r.get(Tables.LINK.LINK_ID),
                r.get(Tables.LINK.LINK_NAME),
                r.get(Tables.LINK.LAST_CHECK),
                r.get(Tables.LINK.LAST_UPDATE),
                r.get(Tables.LINK.LAST_COMMIT),
                r.get(Tables.LINK.AMOUNT_ISSUES),
                r.get(Tables.LINK.TYPE)
            ));
    }
}
