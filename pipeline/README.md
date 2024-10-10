# pipeline

## setup 
````
> sudo pacman -S sbt; yay scala3 
> sudo mkdir /opt/cardig; sudo chown -R $USER:$USER /opt/cardig; cp -r ./data /opt/cardig
````

## resources
````
https://fs2.io/
https://cb372.github.io/cats-retry/
https://typelevel.org/log4cats/
https://pureconfig.github.io/
https://typelevel.org/cats-effect/docs/
https://www.scala-exercises.org/cats/
https://jsoup.org/
````

## dev
````
API found @
https://www.standvirtual.com/api/v1/search?app_iphone=1&json=1&page=2&search[category_id]=29&search[filter_enum_make]=bmw&search[filter_float_price:dec]=33000

search params are in the request json data, in case need to be adjusted
just write business logic code for calling the API, leave the scraper apart for now

# TODO implement https4 requests to model API calls and build dataset
# TODO write data model for training 
https://medium.com/@alandevlin7/http4s-v0-2-1d2d859d86c4

// TODO: replace jsoup with this
// https://github.com/ruippeixotog/scala-scraper

// TODO: make URL a type not String

// TODO: add tests
// https://scalameta.org/munit/docs/integrations/scalacheck.html
// https://scalameta.org/munit/docs/getting-started.html

````
