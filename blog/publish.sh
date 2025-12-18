#! /usr/bin/env nix-shell
#! nix-shell -i bash -p bash pandoc quarto
files=*.org
echo $files

function publish() {
    echo $1
    new_filename="${1%.*}.qmd"
    pandoc -s --wrap=none $1 -t markdown-smart-citations -o $new_filename
}

for filename in $files
do
    echo $filename
    filename=$(basename -- "$filename")
    extension="${filename##*.}"
    filename="${filename%.*}"
    source_file=$filename.org
    target_file=$filename.qmd
    if [ $extension != org ]
    then
        continue
    fi
    if [ $source_file -nt $target_file ]
    then
        printf '%s\n' "$source_file is newer than $target_file"
        publish $source_file
    fi
done

quarto render
rsync -rav --progress _site/* ~/box/Shared/Sites/aoc/
