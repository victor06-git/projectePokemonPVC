# Para archivos en el directorio actual
for f in *; do
  if [ -f "$f" ]; then
    lc=$(echo "$f" | tr '[:upper:]' '[:lower:]')
    if [ "$f" != "$lc" ]; then
      mv "$f" "$lc"
      echo "Renombrado: $f → $lc"
    fi
  fi
done
