function makeSignature() {

  # make signiture
	nl=$'\\n'

	TIMESTAMP=$(echo $(($(date +%s%N)/1000000)))
	ACCESSKEY="F69B46A5BD74794E8737"				# access key id (from portal or Sub Account)
	SECRETKEY="EFF9C27D067B780DF3E9C5707695DF66A46681EF"				# secret key (from portal or Sub Account)

	METHOD="GET"
	URI="https://sourcedeploy.apigw.ntruss.com/api/v1/project"

	SIG="$METHOD"' '"$URI"${nl}
	SIG+="$TIMESTAMP"${nl}
	SIG+="$ACCESSKEY"

	SIGNATURE=$(echo -n -e "$SIG"|iconv -t utf8 |openssl dgst -sha256 -hmac $SECRETKEY -binary|openssl enc -base64)

}