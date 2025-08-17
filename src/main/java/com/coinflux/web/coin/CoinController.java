package com.coinflux.web.coin;

import com.coinflux.web.auth.annotations.LoggedInUser;
import com.coinflux.web.coin.dtos.requests.CreateCoinRequest;
import com.coinflux.web.coin.dtos.requests.GetAllCoinsRequest;
import com.coinflux.web.coin.dtos.responses.CreateCoinResponse;
import com.coinflux.web.coin.dtos.responses.GetAllCoinsResponse;
import com.coinflux.web.coin.dtos.responses.GetCoinResponse;
import com.coinflux.web.user.dtos.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coins")
@RequiredArgsConstructor
public class CoinController {

    private final CoinService coinService;
    @PostMapping
    public ResponseEntity<?> createCoin(@RequestBody CreateCoinRequest request) {
        return new ResponseEntity<>(coinService.createCoin(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCoinById(@PathVariable Long id) {
        return new ResponseEntity<>(coinService.getCoinById(id), HttpStatus.OK);
    }

    @GetMapping("/symbol/{symbol}")
    public ResponseEntity<?> getCoinBySymbol(@LoggedInUser UserDTO loggedInUser,@PathVariable String symbol) {
        System.out.println(loggedInUser);
        return new ResponseEntity<>(coinService.getCoinBySymbol(symbol), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchCoins(@ModelAttribute GetAllCoinsRequest request) {
        return new ResponseEntity<>(coinService.getAllCoins(request), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCoin(@PathVariable Long id) {
        coinService.deleteCoin(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
