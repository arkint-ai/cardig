````
// TODO :: logger to stdout during dev
// TODO: fetch all types of car brands, building a list of pairs [(brand,products)]
// TODO: Scheduler uses Scrapper
// TODO: go deep into each product specs and build a list of pairs [(productTitle,,specs)]
// TODO: save to dated time to the second and keep old snapshots for market analytics (data lake) (for now only keep two versions during development, so write a mechanism to delete the oldest of the three after saving a new one from Scrapper)
// TODO: Scheduler does file IO, and in itself uses Scrapper and Processor combined to fetch data and filter it and saving it to filter
// TODO: each website implementation as a scheduler, which as a list of products to fetch, recursively to fetch the specs and save it to a file in data dir under that scheduler name e.g. standvirtual

````
````

-- Scrapping

--- Product scrapping tutorials
https://www.lihaoyi.com/post/ScrapingWebsitesusingScalaandJsoup.html
https://www.zenrows.com/blog/scala-web-scraping

--- Example code
https://github.com/KadekM/scrawler

-- Docs code
https://typelevel.org/cats/
https://typelevel.org/cats-effect/

````


````

-- Documentation

This changes are sensed using scripts every x minutes (x=5? => have to balance computation cost against worth in market leverage, but the lower the better like 3s)

Scripts in Scala (play with demos and make it work for standvirtual)

Apache spark can run planned jobs (scripts) to aggregate data (batch processing) to feed to ingestion

Scrape logged bidding, buying and selling markets with premium subscriptions if provided, and free as much as possible and send to processing

Further research into spark is needed, hadoop and hive (all apache) are also alternatives?

Can use this to model scrapping categories from standvirtual and maybe other sites too
https://www.standvirtual.com/api/doc/#api-Categories

Scrappers should scrape as much useful data for analytics and business intelligence, better more to process than less, for now, later we can fine tune this to save computation in processing

````
