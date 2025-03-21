package de.neone.simbroker.data.repository.mockdata

import de.neone.simbroker.data.remote.Coin

val coins_Mockdata = listOf(
    Coin(
        uuid = "1",
        symbol = "BTC",
        name = "Bitcoin",
        color = "#f7931a",
        iconUrl = "https://example.com/btc.png",
        price = "67000.0654899",
        marketCap = "1300000000000",
        listedAt = 1234567890L,
        tier = 1,
        change = "-0.2",
        rank = 1,
        sparkline = listOf("67000", "66800", "66500", "66700", "67200", "67100", "66900"),
        lowVolume = false,
        coinrankingUrl = "https://coinranking.com/coin/btc",
        h24Volume = "32000000000",
        description = "The first and largest cryptocurrency",
        priceAt = 1710676800000L,
    ),
    Coin(
        uuid = "2",
        symbol = "ETH",
        name = "Ethereum",
        color = "#3c3c3d",
        iconUrl = "https://example.com/eth.png",
        price = "3500",
        marketCap = "420000000000",
        listedAt = 1434567890L,
        tier = 1,
        change = "1.2",
        rank = 2,
        sparkline = listOf("3400", "3450", "3500", "3480", "3520", "3510", "3500"),
        lowVolume = false,
        coinrankingUrl = "https://coinranking.com/coin/eth",
        h24Volume = "25000000000",
        description = "Decentralized smart contract platform",
        priceAt = 1710676800000L,
    ),
    Coin(
        uuid = "3",
        symbol = "ADA",
        name = "Cardano",
        color = "#0033ad",
        iconUrl = "https://example.com/ada.png",
        price = "0.52",
        marketCap = "40000000000",
        listedAt = 1634567890L,
        tier = 1,
        change = "1.8",
        rank = 3,
        sparkline = listOf("0.50", "0.51", "0.52", "0.515", "0.525", "0.52", "0.522"),
        lowVolume = false,
        coinrankingUrl = "https://coinranking.com/coin/ada",
        h24Volume = "1000000000",
        description = "Third-generation blockchain platform",
        priceAt = 1710676800000L,
    ),
    Coin(
        uuid = "4",
        symbol = "SOL",
        name = "Solana",
        color = "#9933ff",
        iconUrl = "https://example.com/sol.png",
        price = "150",
        marketCap = "70000000000",
        listedAt = 1834567890L,
        tier = 1,
        change = "-3.1",
        rank = 4,
        sparkline = listOf("155", "153", "151", "150", "148", "149", "150"),
        lowVolume = false,
        coinrankingUrl = "https://coinranking.com/coin/sol",
        h24Volume = "3200000000",
        description = "High-performance blockchain",
        priceAt = 1710676800000L,
    ),
    Coin(
        uuid = "5",
        symbol = "XRP",
        name = "Ripple",
        color = "#346aa9",
        iconUrl = "https://example.com/xrp.png",
        price = "0.59",
        marketCap = "25000000000",
        listedAt = 1934567890L,
        tier = 1,
        change = "-0.7",
        rank = 5,
        sparkline = listOf("0.60", "0.59", "0.58", "0.585", "0.59", "0.595", "0.59"),
        lowVolume = false,
        coinrankingUrl = "https://coinranking.com/coin/xrp",
        h24Volume = "500000000",
        description = "Digital payment protocol",
        priceAt = 1710676800000L,
    ),
    Coin(
        uuid = "6",
        symbol = "DOT",
        name = "Polkadot",
        color = "#e6007a",
        iconUrl = "https://example.com/dot.png",
        price = "6.50",
        marketCap = "8000000000",
        listedAt = 2034567890L,
        tier = 1,
        change = "2.3",
        rank = 6,
        sparkline = listOf("6.30", "6.35", "6.40", "6.45", "6.48", "6.50", "6.51"),
        lowVolume = false,
        coinrankingUrl = "https://coinranking.com/coin/dot",
        h24Volume = "1200000000",
        description = "Interoperable blockchain network",
        priceAt = 1710676800000L,
    ),
    Coin(
        uuid = "7",
        symbol = "DOGE",
        name = "Dogecoin",
        color = "#c2a633",
        iconUrl = "https://example.com/doge.png",
        price = "0.12",
        marketCap = "15000000000",
        listedAt = 2134567890L,
        tier = 1,
        change = "-5.6",
        rank = 7,
        sparkline = listOf("0.13", "0.125", "0.122", "0.12", "0.118", "0.12", "0.12"),
        lowVolume = false,
        coinrankingUrl = "https://coinranking.com/coin/doge",
        h24Volume = "700000000",
        description = "Meme coin",
        priceAt = 1710676800000L,
    ),
    Coin(
        uuid = "8",
        symbol = "MATIC",
        name = "Polygon",
        color = "#8247e5",
        iconUrl = "https://example.com/matic.png",
        price = "0.65",
        marketCap = "6500000000",
        listedAt = 2234567890L,
        tier = 1,
        change = "-2.1",
        rank = 8,
        sparkline = listOf("0.67", "0.66", "0.655", "0.65", "0.648", "0.65", "0.65"),
        lowVolume = false,
        coinrankingUrl = "https://coinranking.com/coin/matic",
        h24Volume = "900000000",
        description = "Ethereum scaling solution",
        priceAt = 1710676800000L,
    ),
    Coin(
        uuid = "9",
        symbol = "LTC",
        name = "Litecoin",
        color = "#a6a9aa",
        iconUrl = "https://example.com/ltc.png",
        price = "65.50",
        marketCap = "5000000000",
        listedAt = 2334567890L,
        tier = 1,
        change = "1.9",
        rank = 9,
        sparkline = listOf("64.00", "64.50", "65.00", "65.50", "65.30", "65.40", "65.50"),
        lowVolume = false,
        coinrankingUrl = "https://coinranking.com/coin/ltc",
        h24Volume = "600000000",
        description = "Faster Bitcoin alternative",
        priceAt = 1710676800000L,
    ),
    Coin(
        uuid = "10",
        symbol = "BCH",
        name = "Bitcoin Cash",
        color = "#8dc351",
        iconUrl = "https://example.com/bch.png",
        price = "240.50",
        marketCap = "4700000000",
        listedAt = 2434567890L,
        tier = 1,
        change = "3.0",
        rank = 10,
        sparkline = listOf("233.00", "235.50", "238.00", "240.00", "240.50", "241.00", "240.50"),
        lowVolume = false,
        coinrankingUrl = "https://coinranking.com/coin/bch",
        h24Volume = "700000000",
        description = "Bitcoin fork with faster transactions",
        priceAt = 1710676800000L,
    ),
    Coin(
        uuid = "11",
        symbol = "AVAX",
        name = "Avalanche",
        color = "#e84142",
        iconUrl = "https://example.com/avax.png",
        price = "35.80",
        marketCap = "12500000000",
        listedAt = 2534567890L,
        tier = 1,
        change = "4.2",
        rank = 11,
        sparkline = listOf("34.20", "34.50", "35.00", "35.40", "35.60", "35.80", "35.85"),
        lowVolume = false,
        coinrankingUrl = "https://coinranking.com/coin/avax",
        h24Volume = "850000000",
        description = "Layer 1 blockchain with high throughput",
        priceAt = 1710676800000L,
    ),
    Coin(
        uuid = "12",
        symbol = "LINK",
        name = "Chainlink",
        color = "#2a5ada",
        iconUrl = "https://example.com/link.png",
        price = "15.25",
        marketCap = "9000000000",
        listedAt = 2634567890L,
        tier = 1,
        change = "2.8",
        rank = 12,
        sparkline = listOf("14.80", "14.90", "15.00", "15.10", "15.20", "15.25", "15.22"),
        lowVolume = false,
        coinrankingUrl = "https://coinranking.com/coin/link",
        h24Volume = "580000000",
        description = "Decentralized oracle network",
        priceAt = 1710676800000L,
    ),
    Coin(
        uuid = "13",
        symbol = "UNI",
        name = "Uniswap",
        color = "#ff007a",
        iconUrl = "https://example.com/uni.png",
        price = "8.75",
        marketCap = "6500000000",
        listedAt = 2734567890L,
        tier = 1,
        change = "-1.5",
        rank = 13,
        sparkline = listOf("8.90", "8.85", "8.80", "8.75", "8.72", "8.73", "8.75"),
        lowVolume = false,
        coinrankingUrl = "https://coinranking.com/coin/uni",
        h24Volume = "420000000",
        description = "Decentralized exchange protocol",
        priceAt = 1710676800000L,
    ),
    Coin(
        uuid = "14",
        symbol = "ATOM",
        name = "Cosmos",
        color = "#2e3148",
        iconUrl = "https://example.com/atom.png",
        price = "10.20",
        marketCap = "3800000000",
        listedAt = 2834567890L,
        tier = 1,
        change = "0.8",
        rank = 14,
        sparkline = listOf("10.10", "10.15", "10.18", "10.20", "10.22", "10.21", "10.20"),
        lowVolume = false,
        coinrankingUrl = "https://coinranking.com/coin/atom",
        h24Volume = "320000000",
        description = "Internet of blockchains",
        priceAt = 1710676800000L,
    ),
    Coin(
        uuid = "15",
        symbol = "NEAR",
        name = "NEAR Protocol",
        color = "#000000",
        iconUrl = "https://example.com/near.png",
        price = "6.30",
        marketCap = "3200000000",
        listedAt = 2934567890L,
        tier = 1,
        change = "5.2",
        rank = 15,
        sparkline = listOf("5.95", "6.05", "6.15", "6.20", "6.25", "6.30", "6.32"),
        lowVolume = false,
        coinrankingUrl = "https://coinranking.com/coin/near",
        h24Volume = "280000000",
        description = "Developer-friendly blockchain",
        priceAt = 1710676800000L,
    ),
)