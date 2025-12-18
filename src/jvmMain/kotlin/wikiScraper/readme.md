# Scraping the Wiki

To get a bot token, call `https://starfieldwiki.net/w/api.php?action=query&meta=tokens&type=login&format=json` in a browser, and proceed past the bot check. Then grab the cookie on your request and add it to the config. Note that this cookie doesn't last very long before you get 403'd again
