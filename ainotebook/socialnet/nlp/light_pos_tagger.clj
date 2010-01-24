;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Copyright (c) 2006-2007 Berlin Brown and botnode.com  All Rights Reserved
;;;
;;; http://www.opensource.org/licenses/bsd-license.php

;;; All rights reserved.

;;; Redistribution and use in source and binary forms, with or without modification,
;;; are permitted provided that the following conditions are met:

;;; * Redistributions of source code must retain the above copyright notice,
;;; this list of conditions and the following disclaimer.
;;; * Redistributions in binary form must reproduce the above copyright notice,
;;; this list of conditions and the following disclaimer in the documentation
;;; and/or other materials provided with the distribution.
;;; * Neither the name of the Botnode.com (Berlin Brown) nor
;;; the names of its contributors may be used to endorse or promote
;;; products derived from this software without specific prior written permission.

;;; THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
;;; "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
;;; LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
;;; A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
;;; CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
;;; EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
;;; PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
;;; PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
;;; LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
;;; NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
;;; SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
;;;
;;; Date: 1/5/2009
;;; Main Description: Light bot is a simple IRC/Twitter bot in clojure
;;; Contact: Berlin Brown <berlin dot brown at gmail.com>
;;;
;;; Description: simple part of speech tagger library
;;;              using open-nlp (version 1.4.3)
;;;
;;;
;;; Usage:  java -Xms128m -Xmx200m -classpath $CP -Dlight.install.dir="$INSTALL_DIR" \
;;;               clojure.lang.Script src/light/toolkit/nlp/light_pos_tagger.clj
;;;
;;;
;;; Example Test.txt Input File:
;;;
;;; Hello, my name is Berlin #RESPONSE# My name is light
;;;
;;;
;;; See http://jvmnotebook.googlecode.com/svn/trunk/clojure/light_clojurebot
;;;
;;; Make sure the NLP libaries and model are in the classpath
;;; 
;;; NLP=$INSTALL_DIR/lib/opennlp
;;; NLP1=$NLP/opennlp-tools-1.4.3.jar
;;; NLP2=$NLP/trove.jar
;;; NLP3=$NLP/maxent-2.5.2.jar
;;; NLP_LIB=$NLP1:$NLP2:$NLP3
;;; 
;;; MODELS=$INSTALL_DIR/models
;;; TESTS=$INSTALL_DIR/test/example_docs
;;; DATA=$INSTALL_DIR/data
;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(ns light.toolkit.nlp.light_pos_tagger
  (:use light.toolkit.light_utils)
  (:import
   (java.util.regex Matcher Pattern)
   (java.io BufferedReader InputStreamReader FileInputStream)
   (opennlp.maxent MaxentModel)
   (opennlp.maxent.io SuffixSensitiveGISModelReader)
   (opennlp.tools.dictionary Dictionary)
   (opennlp.tools.postag DefaultPOSContextGenerator)
   (opennlp.tools.postag POSDictionary)
   (opennlp.tools.postag POSTaggerME)
   (opennlp.tools.postag TagDictionary)
   (opennlp.tools.util InvalidFormatException)
   (opennlp.tools.lang.english PosTagger)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def *default-light-response* "I am sorry, I did not understand")

(defn build-model
  "Create the OpenNLP POS Tagger"
  [model-path dict-file]
  ;;;;;;;;;;;;;;;;;;;;;;;
  (let [dict (POSDictionary. dict-file true)]
	(PosTagger. model-path dict)))

(def *core-tag-model* (build-model "models/tag.bin.gz" "models/tagdict"))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def main-pos-tagset
 	 ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	 ;; Map the POS tag to a description and example of the tag.
	 ;;
	 ;; Map of a map ; tag => {:desc "text" :example "" }
	 ;; 45 tags in the Penn tagset
	 ;; 87 in the Brown tagset
	 ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	 {
	  (keyword "ABL")   {:desc "determiner/pronoun"     :example "quite such rather" }
	  (keyword "AT")    {:desc "article"                :example "the an no a" }
	  (keyword "NN")    {:desc "noun, singular, common" :example "failure burden court fire" }
	  (keyword "UH")    {:desc "interjection"           :example "Hurrah bang" }
	  (keyword "VB")    {:desc "verb, base"             :example "find" }
	  (keyword "JJ")    {:desc "adjective"              :example "recent over-all" }
	  (keyword "VBZ")   {:desc "verb, present tense, 3rd person singular" :example "deserves believes" }
	  (keyword "CC")    {:desc "conjuction"             :example "and" }
	  (keyword "PRP$")  {:desc "possessive pronoun"     :example "my his" }
	  (keyword "DT")    {:desc "determiner"             :example "the" }
	  (keyword "CD")    {:desc "cardinal number"        :example "1 2 3" }
	  (keyword "EX")    {:desc "existential there"      :example "there is" }
	  (keyword "JJR")   {:desc "adjective, comparative" :example "greener" }
	  (keyword "JJS")   {:desc "adjective, superlative" :example "greenest" }
	  (keyword "MD")    {:desc "modal"                  :example "could, will" }
	  (keyword "RP")    {:desc "particle"               :example "give up" }
	  (keyword "NNS")   {:desc "noun plural"            :example "tables" }                       
	  (keyword "NNP")   {:desc "proper noun, singular"  :example "John" }
	  (keyword "VB")    {:desc "verb, base form"        :example "take" }
	  (keyword "NNPS")  {:desc "proper noun, plural"    :example "Vikings" }                       
	  (keyword "VBD")   {:desc "verb, past tense"       :example "took" }
	  (keyword "PDT")   {:desc "predeterminer"          :example "both the boys" }                 
	  (keyword "VBG")   {:desc "verb, gerund/present participle"     :example "taking" }
	  (keyword "POS")   {:desc "possessive ending"      :example "friend's" }                     
	  (keyword "VBN")   {:desc "verb, past participle"  :example "taken" }
	  (keyword "PRP")   {:desc "personal pronoun"       :example "I, he, it" }                     
	  (keyword "VBP")   {:desc "verb, sing. present, non-3d"         :example "take" }
	  (keyword "RB")    {:desc "adverb"         :example "however, usually, naturally, here, good" }     
	  (keyword "WDT")   {:desc "wh-determiner"          :example "which" }
	  (keyword "RBR")   {:desc "adverb, comparative"    :example "better" }
	  (keyword "WP")    {:desc "wh-pronoun"             :example "who, what" }
	  (keyword "RBS")   {:desc "adverb, superlative"    :example "best" }
	  (keyword "WP$")   {:desc "possessive wh-pronoun"  :example "whose" }
	  (keyword "WRB")   {:desc "wh-abverb"              :example "where, when" }
	 }) ;; End of tagset

(defn pos-lookup
  "Find the tagset data based on the keyword tag"
  [keyw-tag]
  ;;;;;;;;;;;;
  ((keyword keyw-tag) main-pos-tagset))

(defn pos-lookup-desc
  "Find the tagset data based on the keyword tag"
  [keyw-tag]
  ;;;;;;;;;;;;
  (:desc (pos-lookup keyw-tag)))

(defn pos-lookup-example
  "Find the tagset data based on the keyword tag"
  [keyw-tag]
  ;;;;;;;;;;;;
  (:example (pos-lookup keyw-tag)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn filter-sentence-chars
  "Remove english sentence characters, including question marks, etc"
  [line]
  ;;;;;;;;;;;
  (.replaceAll line "\\.|\\?|,|!|;|:|\"|\\(|\\)" ""))

(defn split-response
  "Split the line to return the input pos tags and response"
  [line]
  ;;;;;;;
  (let [res (.split line "#RESPONSE#")]
	(if (= (count res) 2)
	  {:input-line (light-trim (nth res 0)) :response (light-trim (nth res 1)) }
	  {:input-line line :response nil })))

(defn pos-tag-line
  "Determine the parts of speech tagging for one line.
 Where 'pos-fn' requires three parameters => token -> tag -> pos-data"
  [tagger line pos-fn]
  ;;;;;;;
  (let [tokens   (.split line " ")
		taggings (.tag tagger 1 tokens)]
	  (dotimes [ti   (count taggings)]
		(dotimes [wi (count tokens)]
		  (let [token     (nth tokens wi)
				tag       (nth (nth taggings ti) wi)
				pos-data  (pos-lookup tag)
				desc      (pos-lookup-desc tag)]
			(pos-fn token tag pos-data))))))

(defn pos-tag-response
  "Determine the parts of speech tagging for one line."
  [tagger line]
  ;;;;;;;
  (when (not (empty? line))
	(let [line-map (split-response line)
		  line-in  (line-map :input-line)
		  line-res (line-map :response)
		  vec-line (atom [])]
	  ;;;;;;;;;;;;;;;;;;;;;;;;
	  ;; Determine part of speech tag per this line
	  ;; Add the response data to the line vector.
	  ;; Use 'atom' to mutate the vec-line state.
	  ;;;;;;;;;;;;;;;;;;;;;;;;
	  (pos-tag-line tagger (light-trim (filter-sentence-chars line-in))
					(fn [tok tag data]
					  (swap! vec-line conj
							 {:token    tok 
							  :tag      tag 
							  :pos      data  })))
	  ;; Return line data POS structure
	  {:response line-res :tag-line @vec-line})))
  
(defn load-pos-tag-print
  "Read responses from file and determine the parts of speech"
  [tagger data-file]
  ;;;;;;;;;;;
  (let [in (BufferedReader. (InputStreamReader. (FileInputStream. data-file)))]
	(loop [line (.readLine in)]
	  (when line
		(when (not (empty? line))
		  (let [line-map (split-response line)
				line-in  (line-map :input-line)]
			(pos-tag-line tagger (light-trim (filter-sentence-chars line-in))
						  (fn [tok tag data] (print (str tok "/" tag "#" data "  "))))
			(println)
			(println)))
		(recur (.readLine in))))))

(defn load-pos-tag
  "Read responses from file and determine the parts of speech.
 Return a vector datastructure, part of speech tag each line."
  [tagger data-file]
  ;;;;;;;;;;;
  (let [in (BufferedReader. (InputStreamReader. (FileInputStream. data-file)))
		lines-vec (atom [])]
	(loop [line (.readLine in)]
	  (when line
		(when-let [tag-resp (pos-tag-response tagger line)]
		  (swap! lines-vec conj tag-resp))
		(recur (.readLine in))))
	@lines-vec))

(defn build-tag-key
  "Build a string/keyword datastructure from POS line
 Example output: NN->NN->NN
 
 @param index    index is the result tag map.
 @param pos-vec  input POS data, {:response line-res :tag-line line-pos-map}"
  [pos-vec]
  ;;;;;;;;;;;;;;;
  (let [buf       (StringBuffer.)
		tag-line  (:tag-line pos-vec)
		line-resp (:response pos-vec)]
	(doseq [tag-data tag-line]
	  (.append buf (str (:tag tag-data) "->")))
	(.toString buf)))

(defn index-tag-key
  "Build a string/keyword datastructure from POS line
 Example output: NN->NN->NN
 
 @param index    index is the result tag map.
 @param pos-vec  input POS data, {:response line-res :tag-line line-pos-map}"
  [index pos-vec]
  ;;;;;;;;;;;;;;;
  (let [line-resp (:response pos-vec)]
	;; The tag keyword is built, associate with the response
	(assoc index (keyword (build-tag-key pos-vec))  line-resp)))

(defn build-tag-response-index
  "Build a map data structure with POS keys and a response.
 The POS data is composed of the following:
  :token    Token term
  :tag      POS Tag
  :pos      POS Data Structure"
  [pos-vecs]
  ;;;;;;;;;
  ;; Build the pos response index
  (loop [index    (hash-map)
		 cur-vecs pos-vecs]
	(cond (nil? cur-vecs) index
		  :else (let [pos (first cur-vecs)]
				  (recur (index-tag-key index pos) (rest cur-vecs))))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
		  
(defn simple-pos-test
  ;; Example driver test.  Load the POS data and print the
  ;; Return data structure.
  []
  ;;;;;;;;
  (println "Running simple POS parse")  
  (let [response-index (build-tag-response-index (load-pos-tag *core-tag-model* "test.txt"))
		res-key (build-tag-key (pos-tag-response *core-tag-model* "Hello, my name is Bob, what is your name what"))]
	(println ((keyword res-key) response-index)))
  (println "Done"))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(simple-pos-test)
  
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; End of Script
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;