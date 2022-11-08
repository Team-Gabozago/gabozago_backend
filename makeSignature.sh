function makeSignature() {

  # make signiture
	nl=$'\\n'

	#TIMESTAMP=$(echo $(($(date +%s%N)/1000000)))
	TIMESTAMP=1505290625682
	ACCESSKEY=$1				# access key id (from portal or Sub Account)
	SECRETKEY=$2				# secret key (from portal or Sub Account)

	METHOD="GET"
	URI="https://sourcedeploy.apigw.ntruss.com/api/v1/project"

	SIG="$METHOD"' '"$URI"${nl}
	SIG+="$TIMESTAMP"${nl}
	SIG+="$ACCESSKEY"

	SIGNATURE=$(echo -n -e "$SIG"|iconv -t utf8 |openssl dgst -sha256 -hmac $SECRETKEY -binary|openssl enc -base64)

	#echo ${TIMESTAMP}, ${SIGNATURE}
	#echo $TIMESTAMP
	echo "${TIMESTAMP}"


}