package net.cryptic_game.backend.admin.endpoints.website.blog;

import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.RequiredArgsConstructor;
import net.cryptic_game.backend.admin.authentication.AdminPanelAuthenticator;
import net.cryptic_game.backend.admin.authentication.Permission;
import net.cryptic_game.backend.admin.data.sql.entities.website.blog.BlogEntry;
import net.cryptic_game.backend.admin.data.sql.repositories.website.blog.BlogEntryRepository;
import net.cryptic_game.backend.admin.endpoints.website.WebsiteUtils;
import net.cryptic_game.backend.base.api.annotations.ApiEndpoint;
import net.cryptic_game.backend.base.api.annotations.ApiEndpointCollection;
import net.cryptic_game.backend.base.api.annotations.ApiParameter;
import net.cryptic_game.backend.base.api.data.ApiResponse;
import net.cryptic_game.backend.base.api.data.ApiType;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@ApiEndpointCollection(id = "website/blog", description = "manages blog", type = ApiType.REST, authenticator = AdminPanelAuthenticator.class)
public final class BlogEndpoints {

    private final BlogEntryRepository blogEntryRepository;

    @ApiEndpoint(id = "list")
    public ApiResponse list() {
        return new ApiResponse(HttpResponseStatus.OK, this.blogEntryRepository.findAll());
    }

    @ApiEndpoint(id = "add", authentication = Permission.BLOG_MANAGEMENT)
    public ApiResponse add(@ApiParameter(id = "title") final String title,
                           @ApiParameter(id = "content") final String content) {
        if (WebsiteUtils.checkXxs(title) || WebsiteUtils.checkXxs(content)) {
            return new ApiResponse(HttpResponseStatus.BAD_REQUEST, "NO_HTML_TAGS_ALLOWED");
        }
        return new ApiResponse(HttpResponseStatus.OK, this.blogEntryRepository.save(new BlogEntry(OffsetDateTime.now(), null, title, content)));
    }

    @ApiEndpoint(id = "update", authentication = Permission.BLOG_MANAGEMENT)
    public ApiResponse update(@ApiParameter(id = "id") final UUID id,
                       @ApiParameter(id = "title") final String title,
                       @ApiParameter(id = "content") final String content) {
        if (WebsiteUtils.checkXxs(title) || WebsiteUtils.checkXxs(content)) {
            return new ApiResponse(HttpResponseStatus.BAD_REQUEST, "NO_HTML_TAGS_ALLOWED");
        }
        final Optional<BlogEntry> entry = this.blogEntryRepository.findById(id);
        if (entry.isPresent()) {
            entry.get().setContent(content);
            entry.get().setTitle(title);
            entry.get().setUpdated(OffsetDateTime.now());
            return new ApiResponse(HttpResponseStatus.OK, this.blogEntryRepository.save(entry.get()));
        } else {
            return new ApiResponse(HttpResponseStatus.BAD_REQUEST, "NO_SUCH_ELEMENT");
        }
    }


    @ApiEndpoint(id = "delete", authentication = Permission.BLOG_MANAGEMENT)
    public ApiResponse delete(@ApiParameter(id = "id") final UUID id) {
        final Optional<BlogEntry> entry = this.blogEntryRepository.findById(id);
        if (entry.isEmpty()) {
            return new ApiResponse(HttpResponseStatus.BAD_REQUEST, "NO_SUCH_ELEMENT");
        } else {
            this.blogEntryRepository.delete(entry.get());
            return new ApiResponse(HttpResponseStatus.OK);
        }
    }
}
