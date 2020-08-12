# JSON Message Processing Tool

**Background**: The JSON Message Processing Tool is a tool that was developed for The Boeing Company. I worked with the WGS Anti-Jamming Test/Integration Team that tested the new Anti-Jamming capability. This capability allowed the Wideband Global Satcom(WGS) constellation to avoid jammers by shaping their beam using phased array antennas. 

**Function**: This tool is primarily used to filter out messages from an Apache ActiveMQ (AMQ) databus. It primarily filters messages based on timestamps and keywords.

*Some of the advanced capabilities include*:
 - Regex search
 - start/stop time search
 - start keyword search
 - regular keyworld search
 - Able to intake single/multi line input
 - Able to output single/multi line output

## Project Requirements

```
1. The Parser Tool shall be delivered as a standalone Windows-based executable program.
 - The Parser Tool shall be built as a .jar file
 
2. The Parser Tool shall be configurable via a text file.
 - The Parser Tool shall be configurable using this format:  key=value
 - The Parser Tool config shall be responsible for the input file path.
 - The Parser Tool config shall be responsible for the output file path.
 - The Parser Tool config shall be responsible for the search keyword.
 - The Parser Tool config shall be responsible for the start keyword.
 - The Parser Tool config shall be responsible for the start time.
 - The Parser Tool config shall be responsible for the stop time.
 
3. The Parser Tool shall parse JSON formatted messages from the ActiveMQ bus. (NOTE: JSON messages are provided to the Parser Tool in text file format)
 - The Parser Tool shall be able to parse single-line messages.
 - The Parser Tool shall be able to parse multi-line messages.
 - The Parser Tool shall be able to output single-line messages.
 - The Parser Tool shall be able to output multi-line messages.
 
4. The Parser Tool shall provide the capability to parse JSON formatted messages to include text within a configurable start and stop time.
 - The Parser Tool shall be able to parse the following date format: MM-dd-yyyy hh:mm:ss - 
The Parser Tool shall be able to handle multiple start/stop times.
 
5. The Parser Tool shall provide the capability to parse JSON formatted messages from a configurable start keyword or search string.
 - The Parser Tool shall be able to search the header of a JSON message.
 - The Parser Tool shall be able to search the body of a JSON message.
 - The Parser Tool shall be able to support regular expressions.
 
6. The Parser Tool shall provide the capability to parse JSON formatted messages for a configurable keyword.
 - The Parser Tool shall be able to search the header of a JSON message.
 - The Parser Tool shall be able to search the body of a JSON message.
 - The Parser Tool shall be able to support regular expressions.
 - The Parser Tool shall be able to handle multiple keywords.
 
7. The Parser Tool shall be capable of parsing an input file with up to 1000 JSON formatted messages within no more than 5 minutes.

8. The Parser Tool shall provide error handling.
 - The Parser Tool shall be able to handle config file errors if given a bad config.
 - The Parser Tool shall be able to handle an error if malformed data is provided. 
```
## Installation

**Navigate to the production folder. Inside the production folder, you will find the .jar and any other necessary components.**

## Usage

```
java -jar JSONProcessingTool.jar
```

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License
[MIT](https://choosealicense.com/licenses/mit/)
