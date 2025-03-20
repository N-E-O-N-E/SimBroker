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
        btcPrice = "1.0",
        contractAddresses = listOf("0xBTC"),
        description = "The first and largest cryptocurrency",
        numberOfMarkets = 400,
        numberOfExchanges = 200,
        fullyDilutedMarketCap = "1500000000000",
        priceAt = 1710676800000L,
        tags = listOf("store-of-value", "pow", "bitcoin", "layer-1")
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
        btcPrice = "0.052",
        contractAddresses = listOf("0xETH"),
        description = "Decentralized smart contract platform",
        numberOfMarkets = 500,
        numberOfExchanges = 250,
        fullyDilutedMarketCap = "500000000000",
        priceAt = 1710676800000L,
        tags = listOf("smart-contracts", "ethereum", "layer-1", "pos")
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
        btcPrice = "0.000008",
        contractAddresses = listOf("0xADA"),
        description = "Third-generation blockchain platform",
        numberOfMarkets = 200,
        numberOfExchanges = 150,
        fullyDilutedMarketCap = "45000000000",
        priceAt = 1710676800000L,
        tags = listOf("pos", "cardano", "layer-1", "smart-contracts")
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
        btcPrice = "0.0022",
        contractAddresses = listOf("0xSOL"),
        description = "High-performance blockchain",
        numberOfMarkets = 350,
        numberOfExchanges = 180,
        fullyDilutedMarketCap = "75000000000",
        priceAt = 1710676800000L,
        tags = listOf("high-speed", "solana", "layer-1", "pos")
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
        btcPrice = "0.000008",
        contractAddresses = listOf("0xXRP"),
        description = "Digital payment protocol",
        numberOfMarkets = 150,
        numberOfExchanges = 120,
        fullyDilutedMarketCap = "28000000000",
        priceAt = 1710676800000L,
        tags = listOf("banking", "payment", "ripple")
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
        btcPrice = "0.0001",
        contractAddresses = listOf("0xDOT"),
        description = "Interoperable blockchain network",
        numberOfMarkets = 280,
        numberOfExchanges = 140,
        fullyDilutedMarketCap = "9000000000",
        priceAt = 1710676800000L,
        tags = listOf("interoperability", "substrate", "polkadot", "pos")
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
        btcPrice = "0.000002",
        contractAddresses = listOf("0xDOGE"),
        description = "Meme coin",
        numberOfMarkets = 180,
        numberOfExchanges = 90,
        fullyDilutedMarketCap = "18000000000",
        priceAt = 1710676800000L,
        tags = listOf("meme", "dogecoin", "pow")
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
        btcPrice = "0.00001",
        contractAddresses = listOf("0xMATIC"),
        description = "Ethereum scaling solution",
        numberOfMarkets = 220,
        numberOfExchanges = 110,
        fullyDilutedMarketCap = "7500000000",
        priceAt = 1710676800000L,
        tags = listOf("layer-2", "polygon", "scaling-solution", "ethereum")
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
        btcPrice = "0.001",
        contractAddresses = listOf("0xLTC"),
        description = "Faster Bitcoin alternative",
        numberOfMarkets = 150,
        numberOfExchanges = 80,
        fullyDilutedMarketCap = "5500000000",
        priceAt = 1710676800000L,
        tags = listOf("payments", "litecoin", "pow", "bitcoin-fork")
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
        btcPrice = "0.0036",
        contractAddresses = listOf("0xBCH"),
        description = "Bitcoin fork with faster transactions",
        numberOfMarkets = 170,
        numberOfExchanges = 90,
        fullyDilutedMarketCap = "5000000000",
        priceAt = 1710676800000L,
        tags = listOf("fast-transactions", "bitcoin-cash", "bitcoin-fork", "pow")
    ),

    // Neue Coins (11-30) für ein Gesamttotal von 30 Coins
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
        btcPrice = "0.00053",
        contractAddresses = listOf("0xAVAX"),
        description = "Layer 1 blockchain with high throughput",
        numberOfMarkets = 190,
        numberOfExchanges = 95,
        fullyDilutedMarketCap = "14000000000",
        priceAt = 1710676800000L,
        tags = listOf("layer-1", "avalanche", "smart-contracts", "pos")
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
        btcPrice = "0.00023",
        contractAddresses = listOf("0xLINK"),
        description = "Decentralized oracle network",
        numberOfMarkets = 160,
        numberOfExchanges = 85,
        fullyDilutedMarketCap = "11000000000",
        priceAt = 1710676800000L,
        tags = listOf("oracle", "chainlink", "ethereum", "defi")
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
        btcPrice = "0.00013",
        contractAddresses = listOf("0xUNI"),
        description = "Decentralized exchange protocol",
        numberOfMarkets = 140,
        numberOfExchanges = 75,
        fullyDilutedMarketCap = "7500000000",
        priceAt = 1710676800000L,
        tags = listOf("defi", "dex", "ethereum", "amm")
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
        btcPrice = "0.00015",
        contractAddresses = listOf("0xATOM"),
        description = "Internet of blockchains",
        numberOfMarkets = 130,
        numberOfExchanges = 70,
        fullyDilutedMarketCap = "4200000000",
        priceAt = 1710676800000L,
        tags = listOf("interoperability", "cosmos", "pos", "tendermint")
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
        btcPrice = "0.000094",
        contractAddresses = listOf("0xNEAR"),
        description = "Developer-friendly blockchain",
        numberOfMarkets = 120,
        numberOfExchanges = 65,
        fullyDilutedMarketCap = "3600000000",
        priceAt = 1710676800000L,
        tags = listOf("layer-1", "near", "smart-contracts", "sharding")
    ),
    Coin(
        uuid = "16",
        symbol = "FTM",
        name = "Fantom",
        color = "#1969ff",
        iconUrl = "https://example.com/ftm.png",
        price = "0.62",
        marketCap = "1580000000",
        listedAt = 3034567890L,
        tier = 1,
        change = "3.7",
        rank = 16,
        sparkline = listOf("0.60", "0.605", "0.61", "0.615", "0.62", "0.625", "0.62"),
        lowVolume = false,
        coinrankingUrl = "https://coinranking.com/coin/ftm",
        h24Volume = "210000000",
        btcPrice = "0.000009",
        contractAddresses = listOf("0xFTM"),
        description = "High-speed smart contract platform",
        numberOfMarkets = 110,
        numberOfExchanges = 60,
        fullyDilutedMarketCap = "1800000000",
        priceAt = 1710676800000L,
        tags = listOf("layer-1", "fantom", "smart-contracts", "dag")
    ),
    Coin(
        uuid = "17",
        symbol = "AAVE",
        name = "Aave",
        color = "#b6509e",
        iconUrl = "https://example.com/aave.png",
        price = "95.40",
        marketCap = "1400000000",
        listedAt = 3134567890L,
        tier = 1,
        change = "-2.5",
        rank = 17,
        sparkline = listOf("98.00", "97.50", "96.80", "96.20", "95.80", "95.50", "95.40"),
        lowVolume = false,
        coinrankingUrl = "https://coinranking.com/coin/aave",
        h24Volume = "180000000",
        btcPrice = "0.00142",
        contractAddresses = listOf("0xAAVE"),
        description = "Decentralized lending protocol",
        numberOfMarkets = 100,
        numberOfExchanges = 55,
        fullyDilutedMarketCap = "1600000000",
        priceAt = 1710676800000L,
        tags = listOf("defi", "lending", "ethereum", "yield")
    ),
    Coin(
        uuid = "18",
        symbol = "ALGO",
        name = "Algorand",
        color = "#000000",
        iconUrl = "https://example.com/algo.png",
        price = "0.18",
        marketCap = "1350000000",
        listedAt = 3234567890L,
        tier = 1,
        change = "0.3",
        rank = 18,
        sparkline = listOf("0.179", "0.18", "0.181", "0.18", "0.179", "0.18", "0.18"),
        lowVolume = false,
        coinrankingUrl = "https://coinranking.com/coin/algo",
        h24Volume = "150000000",
        btcPrice = "0.0000027",
        contractAddresses = listOf("0xALGO"),
        description = "Pure proof-of-stake blockchain",
        numberOfMarkets = 95,
        numberOfExchanges = 50,
        fullyDilutedMarketCap = "1800000000",
        priceAt = 1710676800000L,
        tags = listOf("pos", "algorand", "layer-1", "smart-contracts")
    ),
    Coin(
        uuid = "19",
        symbol = "VET",
        name = "VeChain",
        color = "#15bdff",
        iconUrl = "https://example.com/vet.png",
        price = "0.025",
        marketCap = "1800000000",
        listedAt = 3334567890L,
        tier = 1,
        change = "-1.2",
        rank = 19,
        sparkline = listOf("0.0255", "0.0253", "0.0251", "0.025", "0.0249", "0.025", "0.025"),
        lowVolume = false,
        coinrankingUrl = "https://coinranking.com/coin/vet",
        h24Volume = "130000000",
        btcPrice = "0.00000037",
        contractAddresses = listOf("0xVET"),
        description = "Supply chain and IoT blockchain",
        numberOfMarkets = 90,
        numberOfExchanges = 48,
        fullyDilutedMarketCap = "2200000000",
        priceAt = 1710676800000L,
        tags = listOf("supply-chain", "iot", "vechain", "pos")
    ),
    Coin(
        uuid = "20",
        symbol = "FIL",
        name = "Filecoin",
        color = "#0090ff",
        iconUrl = "https://example.com/fil.png",
        price = "5.85",
        marketCap = "2800000000",
        listedAt = 3434567890L,
        tier = 1,
        change = "1.5",
        rank = 20,
        sparkline = listOf("5.75", "5.78", "5.80", "5.82", "5.84", "5.85", "5.85"),
        lowVolume = false,
        coinrankingUrl = "https://coinranking.com/coin/fil",
        h24Volume = "200000000",
        btcPrice = "0.000087",
        contractAddresses = listOf("0xFIL"),
        description = "Decentralized storage network",
        numberOfMarkets = 85,
        numberOfExchanges = 45,
        fullyDilutedMarketCap = "3200000000",
        priceAt = 1710676800000L,
        tags = listOf("storage", "filecoin", "ipfs", "web3")
    ),
    Coin(
        uuid = "21",
        symbol = "XTZ",
        name = "Tezos",
        color = "#2c7df7",
        iconUrl = "https://example.com/xtz.png",
        price = "0.95",
        marketCap = "900000000",
        listedAt = 3534567890L,
        tier = 1,
        change = "-0.8",
        rank = 21,
        sparkline = listOf("0.96", "0.955", "0.95", "0.948", "0.949", "0.95", "0.95"),
        lowVolume = false,
        coinrankingUrl = "https://coinranking.com/coin/xtz",
        h24Volume = "90000000",
        btcPrice = "0.000014",
        contractAddresses = listOf("0xXTZ"),
        description = "Self-amending cryptographic ledger",
        numberOfMarkets = 80,
        numberOfExchanges = 43,
        fullyDilutedMarketCap = "1000000000",
        priceAt = 1710676800000L,
        tags = listOf("pos", "tezos", "smart-contracts", "governance")
    ),
    Coin(
        uuid = "22",
        symbol = "EOS",
        name = "EOS",
        color = "#000000",
        iconUrl = "https://example.com/eos.png",
        price = "0.72",
        marketCap = "800000000",
        listedAt = 3634567890L,
        tier = 1,
        change = "0.5",
        rank = 22,
        sparkline = listOf("0.715", "0.718", "0.72", "0.722", "0.721", "0.72", "0.72"),
        lowVolume = false,
        coinrankingUrl = "https://coinranking.com/coin/eos",
        h24Volume = "85000000",
        btcPrice = "0.000011",
        contractAddresses = listOf("0xEOS"),
        description = "Blockchain with high transaction throughput",
        numberOfMarkets = 75,
        numberOfExchanges = 40,
        fullyDilutedMarketCap = "900000000",
        priceAt = 1710676800000L,
        tags = listOf("dpos", "eos", "smart-contracts", "layer-1")
    ),
    Coin(
        uuid = "23",
        symbol = "MKR",
        name = "Maker",
        color = "#6acebb",
        iconUrl = "https://example.com/mkr.png",
        price = "1450.80",
        marketCap = "1300000000",
        listedAt = 3734567890L,
        tier = 1,
        change = "2.1",
        rank = 23,
        sparkline = listOf("1420.00", "1430.00", "1440.00", "1445.00", "1450.00", "1450.80", "1450.50"),
        lowVolume = false,
        coinrankingUrl = "https://coinranking.com/coin/mkr",
        h24Volume = "75000000",
        btcPrice = "0.0216",
        contractAddresses = listOf("0xMKR"),
        description = "Governance token for the Maker protocol",
        numberOfMarkets = 70,
        numberOfExchanges = 38,
        fullyDilutedMarketCap = "1450000000",
        priceAt = 1710676800000L,
        tags = listOf("defi", "governance", "ethereum", "stablecoin")
    ),
    Coin(
        uuid = "24",
        symbol = "XLM",
        name = "Stellar",
        color = "#000000",
        iconUrl = "https://example.com/xlm.png",
        price = "0.11",
        marketCap = "3100000000",
        listedAt = 3834567890L,
        tier = 1,
        change = "-0.3",
        rank = 24,
        sparkline = listOf("0.111", "0.1105", "0.11", "0.1095", "0.11", "0.11", "0.11"),
        lowVolume = false,
        coinrankingUrl = "https://coinranking.com/coin/xlm",
        h24Volume = "70000000",
        btcPrice = "0.0000016",
        contractAddresses = listOf("0xXLM"),
        description = "Payment network for cross-border transactions",
        numberOfMarkets = 65,
        numberOfExchanges = 35,
        fullyDilutedMarketCap = "3400000000",
        priceAt = 1710676800000L,
        tags = listOf("payment", "stellar", "cross-border", "financial-inclusion")
    ),
    Coin(
        uuid = "25",
        symbol = "THETA",
        name = "Theta Network",
        color = "#2ab8e6",
        iconUrl = "https://example.com/theta.png",
        price = "1.15",
        marketCap = "1150000000",
        listedAt = 3934567890L,
        tier = 1,
        change = "4.5",
        rank = 25,
        sparkline = listOf("1.10", "1.11", "1.12", "1.13", "1.14", "1.15", "1.15"),
        lowVolume = false,
        coinrankingUrl = "https://coinranking.com/coin/theta",
        h24Volume = "65000000",
        btcPrice = "0.000017",
        contractAddresses = listOf("0xTHETA"),
        description = "Decentralized video streaming network",
        numberOfMarkets = 60,
        numberOfExchanges = 33,
        fullyDilutedMarketCap = "1300000000",
        priceAt = 1710676800000L,
        tags = listOf("video", "streaming", "entertainment", "pos")
    )
)