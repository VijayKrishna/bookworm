# Bookworm
Utility to download a specified set of resources from a specified webpage.

## Motivation
This utility was largely born out of a desire to quickly download a set of files, 
whose links/URLs are available in an existing webpage, encapsulated in `<a>` tags.
Two typical use-cases for this utility are: 
- download all the research papers (.pdfs) in a lit-reivew posted on single web-page;
- download all lectures (.pdfs or .ppts) posted on a class website.

#### Why the name "bookworm"?
Because the original intent of this project was to basically dig 
through and fetch reading material like lecture notes or research
papers from a given webpage. 
And so bookworm made a certain amount of sense.
However, I realize (and admit) that the "worm" metaphor does not make sense with the
ideas of "fetching" or "downloading" the reading material.

#### Why Java?
Because, I am an avid Java programmer and Eclipse was already open when i decided to whip this up.
I would probably want to redo this in a scripting language, but this works for me -- at the moment.

## Download, Build and Usage Instructions

Download: `git clone https://github.com/VijayKrishna/bookworm.git && cd bookworm`

Building: Download and use Maven to build this project with the following command: `mvn compile`

Usage: 
```
mvn exec:java -Pbookworm -Dworm.page=<webpage to be parsed for HREFs>\
-Dworm.downloads=<path to downloads directory>\
-Dworm.extn=<reg-ex of the downloadable file-extensions>
```

## Contributions/Contact

Open an issue for getting in touch with me about  bugs, design ideas or feature requests.
And I am also open to pull requests that are preceeded by an open issue in this project (one that you likely opened).

I am also available via email at vpalepu [at] uci [dot] edu.  

## License
The code in this project is licensed under the MIT License. Check the file named "LICENSE" for more details.
