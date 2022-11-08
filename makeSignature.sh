function makeSignature() {
	nl=$'\\n'

	TIMESTAMP=$(echo $(($(date +%s%N)/1000000)))
	ACCESSKEY="{accessKey}"				# access key id (from portal or Sub Account)
	SECRETKEY="{secretKey}"				# secret key (from portal or Sub Account)

	METHOD="GET"
	URI="/photos/puppy.jpg?query1=&query2"

	SIG="$METHOD"' '"$URI"${nl}
	SIG+="$TIMESTAMP"${nl}
	SIG+="$ACCESSKEY"

	SIGNATURE=$(echo -n -e "$SIG"|iconv -t utf8 |openssl dgst -sha256 -hmac $SECRETKEY -binary|openssl enc -base64)
}
