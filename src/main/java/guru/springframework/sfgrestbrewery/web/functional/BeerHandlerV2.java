package guru.springframework.sfgrestbrewery.web.functional;

import guru.springframework.sfgrestbrewery.services.BeerService;
import guru.springframework.sfgrestbrewery.web.model.BeerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class BeerHandlerV2 {
    private final BeerService beerService;

    public Mono<ServerResponse> saveNewBeer(ServerRequest request) {
        Mono<BeerDto> beerDtoMono = request.bodyToMono(BeerDto.class);

        return beerService.saveNewBeerMono(beerDtoMono)
                .flatMap(beerDto -> {
                    return ServerResponse.ok()
                            .header("location", BeerRouterConfig.BEER_V2_URL + "/" + beerDto.getId())
                            .build();
                });
    }

    public Mono<ServerResponse> getBeerByUpc(ServerRequest request) {
        String beerUpc = request.pathVariable("upc");
        //Boolean showInventory = Boolean.valueOf(request.queryParam("showInventory").orElse("false"));

        return beerService.getByUpc(beerUpc)
                .flatMap(beerDto -> {
                    return ServerResponse.ok().bodyValue(beerDto);
                }).switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> getBeerById(ServerRequest request) {
        Integer beerId = Integer.valueOf(request.pathVariable("beerId"));
        Boolean showInventory = Boolean.valueOf(request.queryParam("showInventory").orElse("false"));

        return beerService.getById(beerId, showInventory)
                .flatMap(beerDto -> {
                    return ServerResponse.ok().bodyValue(beerDto);
                }).switchIfEmpty(ServerResponse.notFound().build());
    }
}
